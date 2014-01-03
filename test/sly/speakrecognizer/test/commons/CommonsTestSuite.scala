package sly.speakrecognizer.test.commons

import org.scalatest.Suites
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommonsTestSuite extends Suites (
	new MatrixCompareTest
)