package csvside

import cats.Semigroupal
import cats.data.Validated
import cats.Invariant
import cats.instances.list._

trait RowFormat[A] extends RowReader[A] with RowWriter[A] {
  def ivalidate[B <: A](forward: A => Validated[String, B]): RowFormat[B] =
    ivalidate(forward, identity)

  def ivalidate[B](forward: A => Validated[String, B], reverse: B => A): RowFormat[B] =
    RowFormat[B](this.validate(forward), this.contramap(reverse))
}

object RowFormat {
  def apply[A](reader: RowReader[A], writer: RowWriter[A]): RowFormat[A] =
    new RowFormat[A] {
      def read(row: CsvRow): Validated[List[CsvError], A] =
        reader.read(row)

      def heads: List[CsvPath] =
        writer.heads

      def write(value: A, row: Int): CsvRow =
        writer.write(value, row)
    }

  implicit val rowFormatCartesian: Semigroupal[RowFormat] =
    new Semigroupal[RowFormat] {
      def product[A, B](format1: RowFormat[A], format2: RowFormat[B]): RowFormat[(A, B)] =
        new RowFormat[(A, B)] {
          def read(row: CsvRow): Validated[List[CsvError], (A, B)] = {
            val a = format1.read(row)
            val b = format2.read(row)
            type AllErrorsOr[A] = Validated[List[CsvError], A]
            Semigroupal[AllErrorsOr].product(a, b)
          }

          def heads: List[CsvPath] =
            format1.heads ++ format2.heads

          def write(value: (A, B), row: Int): CsvRow = {
            val row1 = format1.write(value._1, row)
            val row2 = format2.write(value._2, row)
            CsvRow(row, row1.values ++ row2.values)
          }
        }
    }

  implicit val rowFormatFunctor: Invariant[RowFormat] =
    new Invariant[RowFormat] {
      def imap[A, B](format: RowFormat[A])(f: A => B)(g: B => A): RowFormat[B] =
        new RowFormat[B] {
          def read(row: CsvRow): Validated[List[CsvError], B] =
            format.read(row).map(f)

          def heads: List[CsvPath] =
            format.heads

          def write(value: B, row: Int): CsvRow =
            format.write(g(value), row)
        }
    }
}
