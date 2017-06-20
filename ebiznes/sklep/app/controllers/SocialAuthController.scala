package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.impl.providers._
import models.{ RegisteredUser, User }
import models.services.UserService
import dao.RegisteredUserDAO
import play.api.cache.CacheApi
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc._
import utils.auth.DefaultEnv

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * The social auth controller.
 *
 * @param messagesApi The Play messages API.
 * @param silhouette The Silhouette stack.
 * @param userService The user service implementation.
 * @param authInfoRepository The auth info service implementation.
 * @param socialProviderRegistry The social provider registry.
 */
class SocialAuthController @Inject() (
  val messagesApi: MessagesApi,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
  authInfoRepository: AuthInfoRepository,
  socialProviderRegistry: SocialProviderRegistry,
  cache: CacheApi,
  registeredUserDAO: RegisteredUserDAO
)
    extends Controller with I18nSupport with Logger {

  /**
   * Authenticates a user against a social provider.
   *
   * @param provider The ID of the provider to authenticate against.
   * @return The result to display.
   */
  def authenticate(provider: String) = Action.async { r =>
    cacheAuthTokenForOauth1(r) { implicit request =>
      (socialProviderRegistry.get[SocialProvider](provider) match {
        case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
          p.authenticate().flatMap {
            case Left(result) => Future.successful(result)
            case Right(authInfo) => for {
              profile <- p.retrieveProfile(authInfo)
              user <- userService.save(profile)
              authInfo <- authInfoRepository.save(profile.loginInfo, authInfo)
              authenticator <- silhouette.env.authenticatorService.create(profile.loginInfo)
              token <- silhouette.env.authenticatorService.init(authenticator)

            } yield {
              var registeredUser: RegisteredUser = new RegisteredUser(id = 0, userId = user.loginInfo.providerKey, provider = user.loginInfo.providerID,
                firstName = user.firstName.get, lastName = user.lastName.get, fullName = user.fullName.get, email = user.email.get, avatarUrl = user.avatarURL.get, isAdmin = 0)
              registeredUserDAO.insert(registeredUser)
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              var strUser = Json.obj("user" -> user) + ""
              //Ok(Json.obj("user" -> user)).withSession("user" -> strUser)
              Redirect("http://localhost:4200/?auth=" + registeredUser.userId, 302).withSession("user" -> strUser)
            }
          }
        case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
      }).recover {
        case e: ProviderException =>
          logger.error("Unexpected provider error", e)
          Unauthorized(Json.obj("message" -> Messages("could.not.authenticate")))
      }
    }
  }

  def getLoggedUser = Action.async { implicit request =>
    var userId = "unknown"
    request.session.get("user").map { user =>
      {
        val userId1 = user.split("providerKey\":\"").last
        val userId2 = userId1.split("\"},\"firstName").head
        userId = userId2
      }
    }
    registeredUserDAO.getByUserId(userId) map {
      usr => Ok(Json.toJson(usr))
    }
  }

  def logoutUser = Action { implicit request =>
    Ok("Bye").withNewSession
  }

  /**
   * Satellizer executes multiple requests to the same application endpoints for OAuth1.
   *
   * So this function caches the response from the OAuth provider and returns it on the second
   * request. Not nice, but it works as a temporary workaround until the bug is fixed.
   *
   * @param request The current request.
   * @param f The action to execute.
   * @return A result.
   * @see https://github.com/sahat/satellizer/issues/287
   */
  private def cacheAuthTokenForOauth1(request: Request[AnyContent])(f: Request[AnyContent] => Future[Result]): Future[Result] = {
    request.getQueryString("oauth_token") -> request.getQueryString("oauth_verifier") match {
      case (Some(token), Some(verifier)) => cache.get[Result](token + "-" + verifier) match {
        case Some(result) => Future.successful(result)
        case None => f(request).map { result =>
          cache.set(token + "-" + verifier, result, 1 minute)
          result
        }
      }
      case _ => f(request)
    }
  }
}
