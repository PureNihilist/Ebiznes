package views

/**
 * Created by Tomek93 on 2017-05-14.
 */
import javax.inject.Inject
import play.api.http.HttpFilters
import play.filters.cors.CORSFilter

class Filters @Inject() (corsFilter: CORSFilter) extends HttpFilters {
  def filters = Seq(corsFilter)
}
