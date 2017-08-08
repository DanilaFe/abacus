package org.nwapw.abacus.function

/**
 * A data class used for storing information about a function.
 *
 * The Documentation class holds the information necessary to display the information
 * about a function to the user.
 *
 * @param codeName the name of the function as it occurs in code.
 * @param name the name of the function in English.
 * @param description the short description of this function.
 * @param longDescription the full description of this function.
 * @param type the things this documentation maps to.
 */
data class Documentation(val codeName: String, val name: String,
                         val description: String, val longDescription: String,
                         val type: DocumentationType)