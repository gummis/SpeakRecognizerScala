package sly.speakrecognizer.test.hmm.vector

import org.scalatest.Suites
import sly.speakrecognizer.test.hmm.vector.domain.DomainTestSuite
import sly.speakrecognizer.test.hmm.vector.algorithm.AlgorithmsTestSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VectorHmmTestSuite extends Suites(
	new DomainTestSuite,
	new AlgorithmsTestSuite
)
