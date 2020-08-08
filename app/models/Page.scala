package models

/**
  * Klasa pomagająca w stronnicowaniu tabel
  *
  * @param items    elementy na stronie
  * @param page     numer strony
  * @param offset   ilość pominiętych wyników
  * @param total    suma elementów na wszystkich stronach
  * @param pageSize rozmiar strony
  * @tparam A       typ obiektów na stronie
  */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long, var pageSize: Int) {
  lazy val prev: Option[Int] = Option(page - 1).filter(_ >= 0)
  lazy val next: Option[Int] = Option(page + 1).filter(_ => (offset + items.size) < total)
  lazy val pages: Int = {
    val result = total.toFloat / pageSize

    if (result %10 == 0)
      result.toInt
    else
      result.ceil.toInt
  }
}
