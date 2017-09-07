package org.nwapw.abacus.tree

import org.nwapw.abacus.context.MutableReductionContext
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.NumberImplementation

data class EvaluationResult(val value: NumberInterface?, val resultingContext: MutableReductionContext)