package sly.speakrecognizer.test.commons

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MatrixCompareTest extends FunSuite {

  test("Porównywanie macierzy"){
	  val a = Array(
	      Array(1.0,1.0),
	      Array(1.0,1.0)
	  )
	  val b = Array(
	      Array(1.0,1.0),
	      Array(1.0,1.0)
	  )
	  val c = Array(
	      Array(1.0,1.0),
	      Array(1.0,1.0),
	      Array(1.0,1.0)
	  )
	  val d = Array(
	      Array(1.0,1.0,1.0),
	      Array(1.0,1.0,1.0)
	  )
	  val e = Array(
	      Array(1.0,1.0,1.0),
	      Array(1.0,1.0,1.0),
	      Array(1.0,1.0,1.0)
	  )
	  val f = Array(
	      Array(1.0,1.0,1.0),
	      Array(1.0,1.1,1.0),
	      Array(1.0,1.0,1.0)
	  )
	  assert(MatrixCompare.compareMatrixDouble(a, a),"a!=a")
	  assert(MatrixCompare.compareMatrixDouble(a, b),"a!=b")
	  assert(!MatrixCompare.compareMatrixDouble(a, c),"a=c")
	  assert(!MatrixCompare.compareMatrixDouble(a, d),"a=d")
	  assert(!MatrixCompare.compareMatrixDouble(a, e),"a=e")
	  assert(!MatrixCompare.compareMatrixDouble(a, f),"a=f")

	  assert(MatrixCompare.compareMatrixDouble(b, a),"b!=a")
	  assert(MatrixCompare.compareMatrixDouble(b, b),"b!=b")
	  assert(!MatrixCompare.compareMatrixDouble(b, c),"b=c")
	  assert(!MatrixCompare.compareMatrixDouble(b, d),"b=d")
	  assert(!MatrixCompare.compareMatrixDouble(b, e),"b=e")
	  assert(!MatrixCompare.compareMatrixDouble(b, f),"b=f")

	  assert(!MatrixCompare.compareMatrixDouble(c, a),"c=a")
	  assert(!MatrixCompare.compareMatrixDouble(c, b),"c=b")
	  assert(MatrixCompare.compareMatrixDouble(c, c),"c!=c")
	  assert(!MatrixCompare.compareMatrixDouble(c, d),"c=d")
	  assert(!MatrixCompare.compareMatrixDouble(c, e),"c=e")
	  assert(!MatrixCompare.compareMatrixDouble(c, f),"c=f")

	  assert(!MatrixCompare.compareMatrixDouble(d, a),"d=a")
	  assert(!MatrixCompare.compareMatrixDouble(d, b),"d=b")
	  assert(!MatrixCompare.compareMatrixDouble(d, c),"d=c")
	  assert(MatrixCompare.compareMatrixDouble(d, d),"d!=d")
	  assert(!MatrixCompare.compareMatrixDouble(d, e),"d=e")
	  assert(!MatrixCompare.compareMatrixDouble(d, f),"d=f")

	  assert(!MatrixCompare.compareMatrixDouble(e, a),"e=a")
	  assert(!MatrixCompare.compareMatrixDouble(e, b),"e=b")
	  assert(!MatrixCompare.compareMatrixDouble(e, c),"e=c")
	  assert(!MatrixCompare.compareMatrixDouble(e, d),"e=d")
	  assert(MatrixCompare.compareMatrixDouble(e, e),"e!=e")
	  assert(!MatrixCompare.compareMatrixDouble(e, f),"e=f")

	  assert(!MatrixCompare.compareMatrixDouble(f, a),"f=a")
	  assert(!MatrixCompare.compareMatrixDouble(f, b),"f=b")
	  assert(!MatrixCompare.compareMatrixDouble(f, c),"f=c")
	  assert(!MatrixCompare.compareMatrixDouble(f, d),"f=d")
	  assert(!MatrixCompare.compareMatrixDouble(f, e),"f=e")
	  assert(MatrixCompare.compareMatrixDouble(f, f),"f!=f")

	}
}