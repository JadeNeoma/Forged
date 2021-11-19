/*
 *     Forged, A platform for creating TTRPG systems in a growth style.
 *     Copyright (C) 2021  Jade Neoma info@MidnightWick.Com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.midnightwick.forged

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        println("elephant")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val system = getSystem()
        println(system.calcMod(0))
        println("Tiger")

        // Fill Spinners
        val spinner: Spinner = this.findViewById(R.id.spinnerA1)
        if (spinner != null) {
            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item, system.listSkills()
            )
            spinner.setAdapter(adapter)

        }
    }

    private fun getSystem(): System { // TODO: Create System Saving, export, import, and retrieval
        val system = createSystem()
        return system
    }

    fun createSystem(): System { // Creation of new system
        val system = System( // test system, delete before release TODO: Add actual creation system
            "testSystem",
            listOf(Skill("testSkill", 123), Skill("testSkill", arrayOf(arrayOf(0, 1)), 0)),
            50
        )
        return system
    }

}

class Skill( // Skill class made for use inside a list, they will normally be addressed by the list index
    public val name: String,
    // anchors: Array<Array<Int>>, // All Skills need at least 3 anchors in order to be properly mapped
    public var exp: Int = 0 // experience level of the skill
)

class System(
    name: String,
    private val skills: List<Skill>,
    per: Int
    // TODO: Create table of all skill links and function to auto update table
) {

    fun listSkills(): Array<String> { // Convert list of skill names into an array for use
        val skillNames = arrayListOf<String>()
        for (i in skills.indices) {
            val skill = skills[i]
            skillNames.add(skill.name)
        }
        return skillNames.toTypedArray()
    }

    fun calcMod(skill: Int): Int { // Calculates modifier for dice roll, based on a percentile roll
        val exp = skills[skill].exp
        return 0 - (exp / 20) // Modifier is negative and subtracted from overall roll
    }
}
