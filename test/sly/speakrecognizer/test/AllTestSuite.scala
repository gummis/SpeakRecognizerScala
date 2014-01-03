package sly.speakrecognizer.test

import org.scalatest.Suites
import sly.speakrecognizer.test.hmm.vector.VectorHmmTestSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import sly.speakrecognizer.test.commons.CommonsTestSuite

@RunWith(classOf[JUnitRunner])
class AllTestSuite extends Suites(
		new VectorHmmTestSuite,
		new CommonsTestSuite
    )
