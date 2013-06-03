package pparkkin.games.immortals

import org.scalatest.FunSuite

class TestApp extends FunSuite {
	test("foo concatenates simple args") {
	  val args = Array[String]("a", "b")
	  expectResult("ab") {
	    App.foo(args)
	  }
	}
}
