package org.nwapw.abacus

import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.number.NumberInterface

data class EvaluationResult(val value: NumberInterface, val resultingContext: MutableEvaluationContext)