package sly.speakrecognizer.test.hmm.vector.algorithm

import org.scalatest.Suites
import sly.speakrecognizer.test.hmm.vector.domain.HmmAndOpdfTest

class AlgorithmsTestSuite extends Suites(
  new AlphaCalculatorTest,
  new BetaCalculatorTest
)