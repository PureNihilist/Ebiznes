package models

import java.sql.Timestamp

case class Purchase(id: Long, userId: String, adress: String, sendType: String, jsonOrder: String, totalPrice: Double, date: String)
