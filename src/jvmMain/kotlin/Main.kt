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


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberDialogState

fun main() = application {
    var winTitle by remember { mutableStateOf("") }
    var ttrpg by remember { mutableStateOf(TTRPG("", "")) }
    Window(
        title = winTitle,
        onCloseRequest = ::exitApplication
    ) {
        var isNewDialogOpen by remember { mutableStateOf(false) }
        var isCopyrightDialogOpen by remember { mutableStateOf(false) }
        var notImplemented by remember { mutableStateOf(false) }
        var isLoaded by remember { mutableStateOf(false) }
        var systemName by remember { mutableStateOf("Example System") }
        var authorName by remember { mutableStateOf("Example Author") }

        MenuBar {
            Menu("File") {
                Item("New", onClick = { isNewDialogOpen = true })
                Item("Load", onClick = { notImplemented = true }) // TODO: Add loading functionality
                if (isLoaded) {
                    Item("Save", onClick = { notImplemented = true })
                }
                Item("Exit", onClick = { exitApplication() })
            }

            if (isLoaded) {
                Menu("Edit") {
                    Item("Create Web", onClick = {
                        ttrpg.webs.add(Web("", "", "", ""))
                    })
                }
            }

            Menu("Help") {
                Item("Manual", onClick = { notImplemented = true }) // TODO: Add manual link
                Item("Copyright", onClick = { isCopyrightDialogOpen = true })
            }

            if (isCopyrightDialogOpen) {
                Dialog(
                    title = "Copyright",
                    onCloseRequest = { isCopyrightDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    Text(
                        "Copyright (c) 2023 Jade Neoma\n" +
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

            if (notImplemented) {
                Dialog(
                    onCloseRequest = { notImplemented = false }
                ) {
                    Text("Not Implemented")
                }
            }

            if (isNewDialogOpen) {
                Dialog(
                    title = "New System",
                    onCloseRequest = { isNewDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    var load by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(50.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
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
                            content = { Text("Create System!") },
                            onClick = { load = true },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )

                        if (load) {
                            isNewDialogOpen = false
                            isLoaded = true
                            winTitle = systemName
                        }

                    }
                }
            }
        }
        // Main App
        if (isLoaded) {
            MaterialTheme {
                ttrpg = remember({ TTRPG(systemName, authorName) })
                print(ttrpg.toString())
                var webSelected by remember { mutableStateOf(0) }

                Box { // Expanded Drop Down Substitute
                    var expanded by remember { mutableStateOf(false) }
                    Button(
                        onClick = { expanded = true }
                    ) {
                        if (mutableStateOf(ttrpg.webs.size).value != 0) {
                            Text(ttrpg.webs[webSelected].name)
                        } else {
                            Text("Edit -> New Web")
                        }
                    }
                }
            }
        }


    }

}

class Anchor(
    var name: String,
    val x: Double,
    val y: Double,
) {
    override fun toString(): String {
        return "$name: ($x, $y)"
    }
}

class Web(
    var name: String,
    anchor0: String,
    anchor1: String,
    anchor2: String
) {
    lateinit var anchors: SnapshotStateList<Anchor>

    init {
        anchors =
            mutableStateListOf(
                Anchor(anchor0, 0.0, 433.013),
                Anchor(anchor1, 500.0, -433.013),
                Anchor(anchor2, -500.0, -433.013)
            )
    }

    public fun createAnchor() {

    }


    override fun toString(): String {
        return "name: $name\n anchors:\n ${anchors.toString()}"
    }
}

class TTRPG(
    var name: String,
    var author: String,
    var webs: SnapshotStateList<Web> = mutableStateListOf()
) {
    init {
        val created = System.currentTimeMillis()
    }

    override fun toString(): String {
        return "name: $name\n author: $author\n Webs:\n $webs "
    }

}