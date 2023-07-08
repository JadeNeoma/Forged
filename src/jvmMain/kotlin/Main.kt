/*
 *      Copyright (c) 2023 Jade Neoma <Joung3010@gmail.com>
 *
 *
 *      This file is part of Forged.
 *
 *      Forged is free software: you can redistribute it and/or
 *      modify it under the terms of the GNU General Public
 *      License as published by the Free Software Foundation,
 *      either version 3 of the License, or (at your option) any
 *      later version.
 *
 *      Forged is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the
 *      implied warranty of MERCHANTABILITY or FITNESS
 *      FOR A PARTICULAR PURPOSE. See the GNU General
 *      Public License for more details.
 *
 *      You should have received a copy of the GNU General
 *      Public License along with Forged. If not, see
 *      <https://www.gnu.org/licenses/>.
 */


import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

@Composable
@Preview
fun App(system : TTRPG) {
    MaterialTheme {

    }
}

@Composable
@Preview
fun NewSystemDialog(){
    MaterialTheme {
        var systemName by remember { mutableStateOf("Example System") }
        var authorName by remember { mutableStateOf("Example Author") }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(50.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = systemName,
                onValueChange = { systemName = it },
                label = { Text(text = "System Name") },
                )

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = authorName,
                onValueChange = { authorName = it },
                label = { Text(text = "Author") },
            )

            Button(
                content = {Text("Create System!")},
                onClick = {createNew(systemName, authorName)},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun createNew(systemName: String, authorName: String){
    App(TTRPG(systemName, authorName))
}

fun main() = application {
    Window(

        onCloseRequest = ::exitApplication
    ) {

        var isNewDialogOpen by remember { mutableStateOf(false) }
        var isCopyrightDialogOpen by remember { mutableStateOf(false) }

        MenuBar {
            Menu("File"){
                Item("New", onClick = { isNewDialogOpen = true })
                Item("Load", onClick = {}) // TODO: Add loading functionality
                Item("Exit", onClick = {exitApplication()})
            }

            Menu("Help"){
                Item("Manual", onClick = {})
                Item("Copyright", onClick = {isCopyrightDialogOpen = true}) // TODO: Add loading functionality
            }

            if (isCopyrightDialogOpen) {
                Dialog(
                    title = "Copyright",
                    onCloseRequest = {isCopyrightDialogOpen = false},
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ){
                    Text("Copyright (c) 2023 Jade Neoma\n" +
                            "Forged is free software: you can redistribute it and/or\n" +
                            "modify it under the terms of the GNU General Public\n" +
                            "License as published by the Free Software Foundation,\n" +
                            "either version 3 of the License, or (at your option) any\n" +
                            "later version.\n" +
                            "\n" +
                            "Forged is distributed in the hope that it will be useful,\n" +
                            "but WITHOUT ANY WARRANTY; without even the\n" +
                            "implied warranty of MERCHANTABILITY or FITNESS\n" +
                            "FOR A PARTICULAR PURPOSE. See the GNU General\n" +
                            "Public License for more details.\n" +
                            "\n" +
                            "You should have received a copy of the GNU General\n" +
                            "Public License along with Forged. If not, see\n" +
                            "<https://www.gnu.org/licenses/>."
                    )
                }
            }

            if (isNewDialogOpen) {
                Dialog(
                    title = "New System",
                    onCloseRequest = {isNewDialogOpen = false},
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ){
                    NewSystemDialog()
                }
            }
        }
    }
}

class Anchor(
    var name:String,
    val x: Double,
    val y: Double,
){
    override fun toString(): String {
        return "$name: $x, $y"
    }
}

class Web(
    var name: String,
    anchor0: String,
    anchor1: String,
    anchor2: String
){
    var anchors: MutableList<Anchor> =
        mutableListOf(
            Anchor(anchor0, 0.0, 433.013),
            Anchor(anchor1, 500.0, -433.013),
            Anchor(anchor2, -500.0, -433.013) )

    public fun createAnchor() {

    }


    override fun toString(): String {
        return "name: $name\n anchors:\n $anchors"
    }
}

class TTRPG(
    var name: String,
    var author: String,
    var webs: MutableList<Web> = mutableListOf()
){
    init {
        val created = System.currentTimeMillis()
    }

    override fun toString(): String {
        return "name: $name\n author: $author\n Webs:\n $webs "
    }

}