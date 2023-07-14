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


import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberDialogState
import androidx.compose.ui.window.rememberWindowState
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.awt.GraphicsEnvironment
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random


val DISPLAYWIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode.width
val DISPLAYHEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode.height

@Preview
fun main() = application {
    var DarkTheme by remember { mutableStateOf(true) }
    var winTitle by remember { mutableStateOf("") }
    var winState = rememberWindowState(
        position = WindowPosition(Alignment.CenterStart),
        height = (DISPLAYHEIGHT - 50).dp, width = (DISPLAYWIDTH - 50).dp
    )
    var ttrpg by remember { mutableStateOf(TTRPG("", "")) }
    var party by remember { mutableStateOf(Party("")) }
    Window(
        title = winTitle,
        onCloseRequest = ::exitApplication,
        state = winState
    ) {
        MaterialTheme {
            var isNewTTRPGDialogOpen by remember { mutableStateOf(false) }
            var isNewPartyDialogOpen by remember { mutableStateOf(false) }
            var isCopyrightDialogOpen by remember { mutableStateOf(false) }
            var isNewWebDialogOpen by remember { mutableStateOf(false) }
            var isNewAnchorDialogOpen by remember { mutableStateOf(false) }
            var isNewCharacterDialogOpen by remember { mutableStateOf(false) }
            var isSaveDialogOpen by remember { mutableStateOf(false) }
            var isLoadDialogOpen by remember { mutableStateOf(false) }
            var isSelectAnchorsDialogOpen by remember { mutableStateOf(false) }

            var notImplemented by remember { mutableStateOf(false) }
            var isTTRPGLoaded by remember { mutableStateOf(false) }
            var isPartyLoaded by remember { mutableStateOf(false) }

            var systemName by remember { mutableStateOf("Example System") }
            var authorName by remember { mutableStateOf("Example Author") }

            MenuBar {

                // Menu Bar Items
                Menu("File") {
                    Item("New System", onClick = { isNewTTRPGDialogOpen = true })
                    Item("New Party", onClick = { isNewPartyDialogOpen = true })
                    Item("Load", onClick = { isLoadDialogOpen = true })
                    if (isTTRPGLoaded || isPartyLoaded) {
                        Item("Save", onClick = { isSaveDialogOpen = true })
                    }
                    // Item("Swap Theme", onClick = { DarkTheme = DarkTheme.not() })
                    Item("Exit", onClick = { exitApplication() })
                }

                if (isTTRPGLoaded || isPartyLoaded) {
                    Menu("Edit") {
                        if (isTTRPGLoaded) {
                            Item("Create Web", onClick = { isNewWebDialogOpen = true })
                            if (ttrpg.webs.size != 0) {
                                Item("Create Anchor", onClick = { isNewAnchorDialogOpen = true })
                            }
                        }
                        if (isPartyLoaded) {
                            Item("Create New Character", onClick = { isNewCharacterDialogOpen = true })
                        }
                    }
                }

                Menu("Help") {
                    Item("Manual", onClick = { notImplemented = true }) // TODO: Add manual link
                    Item("Copyright", onClick = { isCopyrightDialogOpen = true })
                }
            }

            // Dialog Boxes
            if (isCopyrightDialogOpen) {
                Dialog(
                    title = "Copyright",
                    onCloseRequest = { isCopyrightDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(25.dp)
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
            }

            if (isSaveDialogOpen) {
                Dialog(
                    title = "Save",
                    onCloseRequest = { isSaveDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    var isSuccessDialogOpen by remember { mutableStateOf(false) }
                    var ttrpgChecked by remember { mutableStateOf(false) }
                    var saveName by remember { mutableStateOf("") }
                    var submit by remember { mutableStateOf(false) }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(25.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Save Party", modifier = Modifier.padding(top = 15.dp))
                            Switch(
                                checked = ttrpgChecked,
                                onCheckedChange = {
                                    ttrpgChecked = it
                                }
                            )
                            Text("Save System", modifier = Modifier.padding(top = 15.dp))
                        }

                        TextField(
                            value = saveName,
                            label = { Text("Save Name:") },
                            onValueChange = { saveName = it }
                        )

                        Button(
                            onClick = { submit = true },
                            content = { Text("Save") }
                        )

                        if (submit && saveName.isBlank().not()) {
                            isSuccessDialogOpen = if (ttrpgChecked) {
                                save(ttrpg, "src/jvmMain/resources/Saves/Systems/$saveName")
                            } else {
                                save(party, "src/jvmMain/resources/Saves/Parties/$saveName")
                            }
                        }

                        if (isSuccessDialogOpen) {
                            Dialog(
                                title = "Saved Successfully",
                                onCloseRequest = {
                                    isSuccessDialogOpen = false
                                    isSaveDialogOpen = false
                                }
                            ) {
                                Text("Success")
                            }
                        }

                    }
                }
            }

            if (isLoadDialogOpen) {
                Dialog(
                    title = "Load",
                    onCloseRequest = { isLoadDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    var ttrpgChecked by remember { mutableStateOf(false) }
                    var nameExpanded by remember { mutableStateOf(false) }
                    var saveName by remember { mutableStateOf("") }
                    var saveList by remember { mutableStateOf(mutableListOf<Path>()) }
                    var submit by remember { mutableStateOf(false) }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(25.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text("Save Party", modifier = Modifier.padding(top = 15.dp))
                            Switch(
                                checked = ttrpgChecked,
                                onCheckedChange = {
                                    ttrpgChecked = it
                                    saveName = ""
                                }
                            )
                            Text("Save System", modifier = Modifier.padding(top = 15.dp))
                        }

                        val path = if (ttrpgChecked) {
                            "src/jvmMain/resources/Saves/Systems/"
                        } else {
                            "src/jvmMain/resources/Saves/Parties/"
                        }

                        val tempList = mutableListOf<Path>()

                        Files.walk(Paths.get(path)).use { paths ->
                            paths.filter { Files.isRegularFile(it) }
                                .forEach {
                                    tempList.add(it)
                                }
                            saveList = tempList
                        }

                        Button(
                            onClick = {
                                if (saveList.isEmpty().not()) {
                                    nameExpanded = true
                                }
                            },
                            content = {
                                if (saveList.isEmpty()) {
                                    Text("No Saves Found")
                                } else if (saveName.isBlank()) {
                                    Text("Select Save")
                                } else {
                                    Text(saveName)
                                }
                            }
                        )

                        DropdownMenu(
                            expanded = nameExpanded,
                            onDismissRequest = { nameExpanded = false }
                        ) {
                            saveList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        saveName = it.toString()
                                        nameExpanded = false
                                    }
                                ) {
                                    Text(it.fileName.toString())
                                }
                            }
                        }

                        Button(
                            onClick = { submit = true },
                            content = { Text("Load") }
                        )

                        if (submit && saveName.isBlank().not()) {
                            if (ttrpgChecked) {
                                ttrpg = remember { load(saveName) as TTRPG }
                                print(ttrpg)
                                isTTRPGLoaded = true
                            } else {
                                party = remember { load(saveName) as Party }
                                isPartyLoaded = true
                            }
                        }
                    }
                }
            }

            if (notImplemented) {
                Dialog(
                    onCloseRequest = { notImplemented = false }
                ) {
                    Text("Not Implemented")
                }
            }

            if (isNewWebDialogOpen) {
                Dialog(
                    title = "${ttrpg.name}: New Web",
                    onCloseRequest = { isNewWebDialogOpen = false },
                    state = rememberDialogState(
                        position = WindowPosition(Alignment.Center),
                        400.dp, 450.dp
                    ),
                    resizable = true,
                ) {
                    var submit by remember { mutableStateOf(false) }
                    var webName by remember { mutableStateOf("Example Web") }
                    var anchor0 by remember { mutableStateOf("Anchor 0") }
                    var anchor1 by remember { mutableStateOf("Anchor 1") }
                    var anchor2 by remember { mutableStateOf("Anchor 2") }
                    Box(modifier = Modifier.padding(50.dp)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .padding(25.dp)
                        ) {
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = webName,
                                onValueChange = { webName = it },
                                label = { Text(text = "Web Name") },
                            )

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = anchor0,
                                onValueChange = { anchor0 = it },
                                label = { Text(text = "1st Anchor") },
                            )

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = anchor1,
                                onValueChange = { anchor1 = it },
                                label = { Text(text = "2nd Anchor") },
                            )

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = anchor2,
                                onValueChange = { anchor2 = it },
                                label = { Text(text = "3rd Anchor") },
                            )

                            Button(
                                content = { Text("Create Web!") },
                                onClick = { submit = true },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )

                            if (submit) {
                                isNewWebDialogOpen = false
                                ttrpg.webs.add(Web(webName, anchor0, anchor1, anchor2))
                            }

                        }
                    }
                }
            }

            if (isNewAnchorDialogOpen) {
                Dialog(
                    title = "${ttrpg.name}: New Anchor",
                    onCloseRequest = { isNewAnchorDialogOpen = false },
                    state = rememberDialogState(
                        position = WindowPosition(Alignment.Center),
                        600.dp, 700.dp
                    ),
                    resizable = true
                ) {
                    var newAnchorName by remember { mutableStateOf("Example Anchor") }

                    var webExpanded by remember { mutableStateOf(false) }
                    var webSelection by remember { mutableStateOf(-1) }

                    var anchor0Selection by remember { mutableStateOf(-1) }
                    var anchor0Difference by remember { mutableStateOf(1000.0) }
                    var anchor0Expanded by remember { mutableStateOf(false) }

                    var anchor1Selection by remember { mutableStateOf(-1) }
                    var anchor1Difference by remember { mutableStateOf(1000.0) }
                    var anchor1Expanded by remember { mutableStateOf(false) }
                    var anchor1MaxDiff by remember { mutableStateOf(1999.0) }
                    var anchor1MinDiff by remember { mutableStateOf(1.0) }

                    var anchor2Selection by remember { mutableStateOf(-1) }
                    var anchor2Expanded by remember { mutableStateOf(false) }

                    var optionSelected by remember { mutableStateOf(-1) }
                    var optionExpanded by remember { mutableStateOf(false) }
                    // var intersects by remember { mutableStateOf( mutableListOf( listOf(0.0,0.0)) )}
                    // var options by remember { mutableStateOf(mutableListOf(0.0,0.0)) }

                    var newAnchorCoords by remember { mutableStateOf(listOf(0.0, 0.0)) }

                    var submit by remember { mutableStateOf(false) }


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(25.dp)
                    ) {

                        // Web selection
                        Box(
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Button(
                                content = {
                                    if (webSelection == -1) {
                                        Text("Select Web")
                                    } else {
                                        Text("Web: ${ttrpg.webs[webSelection].name}")
                                    }
                                },
                                onClick = {
                                    webExpanded = true
                                }
                            )
                            DropdownMenu(
                                onDismissRequest = { webExpanded = false },
                                expanded = webExpanded
                            ) {
                                ttrpg.webs.forEachIndexed { webIndex, eachWeb ->
                                    DropdownMenuItem(
                                        onClick = {
                                            webSelection = webIndex
                                            webExpanded = false
                                        },
                                    ) {
                                        Text(eachWeb.name)
                                    }
                                }
                            }
                        }

                        TextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            value = newAnchorName,
                            onValueChange = { newAnchorName = it },
                            label = { Text(text = "Anchor Name:") },
                        )

                        // First Anchor
                        Box(
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Button(
                                content = {
                                    if (webSelection == -1) {
                                        Text("Select Web First!")
                                    } else if (anchor0Selection == -1) {
                                        Text("First Anchor")
                                    } else {
                                        Text("Anchor: ${ttrpg.webs[webSelection].anchors[anchor0Selection].name}")
                                    }
                                },
                                onClick = { anchor0Expanded = true },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            )
                            if (webSelection != -1) {
                                DropdownMenu(
                                    onDismissRequest = { anchor0Expanded = false },
                                    expanded = anchor0Expanded
                                ) {
                                    ttrpg.webs[webSelection].anchors.forEachIndexed { anchorIndex, eachAnchor ->
                                        if (anchorIndex != anchor1Selection && anchorIndex != anchor2Selection) {

                                            DropdownMenuItem(
                                                onClick = {
                                                    anchor0Selection = anchorIndex
                                                    anchor0Expanded = false
                                                }) {
                                                Text(eachAnchor.name)
                                            }
                                        }
                                    }
                                    Divider(startIndent = 1.dp, thickness = 1.dp, color = Color.Black)
                                    DropdownMenuItem(
                                        onClick = {
                                            anchor0Selection = -1
                                            anchor1Selection = -1
                                            anchor2Selection = -1
                                        }
                                    ) {
                                        Text("RESET")
                                    }
                                }
                            }
                        }

                        Box(
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(25.dp, 0.dp)
                                    .fillMaxWidth()
                            ) {
                                var advanced by remember { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier
                                        .padding(0.dp, 0.dp, 0.dp, 10.dp)
                                ) {
                                    Text(
                                        "Advanced:",
                                        modifier = Modifier.padding(0.dp, 15.dp, 5.dp, 0.dp)
                                    )
                                    Switch(
                                        checked = advanced,
                                        onCheckedChange = {
                                            advanced = advanced.not()
                                        }
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.TopCenter,
                                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
                                ) {

                                    if (advanced.not()) {
                                        Text("Difference: ${(anchor0Difference).toInt()}")
                                        Slider(
                                            valueRange = 1f..1999f,
                                            value = anchor0Difference.toFloat(),
                                            onValueChange = { anchor0Difference = it.toDouble() },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(25.dp, 10.dp)
                                        )
                                    } else {
                                        Box(
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            TextField(
                                                value = (anchor0Difference).toInt().toString(),
                                                onValueChange = {
                                                    anchor0Difference = if (it.matches(Regex("^[0-9]+\$"))) {
                                                        it.toDouble()
                                                    } else {
                                                        0.5
                                                    }
                                                },
                                                label = { Text(text = "Anchor Name:") },
                                            )
                                        }
                                    }


                                }

                            }
                        }

                        // Second anchor
                        Box(
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Button(
                                content = {
                                    if (webSelection == -1) {
                                        Text("Select Web First!")
                                    } else if (anchor0Selection == -1) {
                                        Text("Select first anchor first")
                                    } else if (anchor1Selection == -1) {
                                        Text("Second anchor")
                                    } else {
                                        Text("Anchor: ${ttrpg.webs[webSelection].anchors[anchor1Selection].name}")
                                    }
                                },
                                onClick = { anchor1Expanded = true },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            )
                            if ((webSelection != -1) && (anchor0Selection != -1)) {
                                DropdownMenu(
                                    onDismissRequest = { anchor1Expanded = false },
                                    expanded = anchor1Expanded
                                ) {
                                    ttrpg.webs[webSelection].anchors.forEachIndexed { anchorIndex, eachAnchor ->
                                        if (anchorIndex != anchor0Selection && anchorIndex != anchor2Selection) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    anchor1Selection = anchorIndex
                                                    anchor1Expanded = false
                                                }) {
                                                Text(eachAnchor.name)
                                            }
                                        }
                                    }
                                    Divider(startIndent = 1.dp, thickness = 1.dp, color = Color.Black)
                                    DropdownMenuItem(
                                        onClick = {
                                            anchor1Selection = -1
                                            anchor2Selection = -1
                                        }
                                    ) {
                                        Text("RESET")
                                    }
                                }
                            }
                        }

                        Box(
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(25.dp, 0.dp)
                                    .fillMaxWidth()
                            ) {
                                var advanced by remember { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier
                                        .padding(0.dp, 0.dp, 0.dp, 10.dp)
                                ) {
                                    Text(
                                        "Advanced:",
                                        modifier = Modifier.padding(0.dp, 15.dp, 5.dp, 0.dp)
                                    )
                                    Switch(
                                        checked = advanced,
                                        onCheckedChange = {
                                            advanced = advanced.not()
                                        }
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.TopCenter,
                                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
                                ) {
                                    if (anchor1Selection != -1) {
                                        anchor1MinDiff = abs(
                                            dist(
                                                ttrpg.webs[webSelection].anchors[anchor0Selection].x,
                                                ttrpg.webs[webSelection].anchors[anchor0Selection].y,
                                                ttrpg.webs[webSelection].anchors[anchor1Selection].x,
                                                ttrpg.webs[webSelection].anchors[anchor1Selection].y
                                            ) - (anchor0Difference)
                                        )
                                        anchor1MaxDiff = abs(
                                            dist(
                                                ttrpg.webs[webSelection].anchors[anchor0Selection].x,
                                                ttrpg.webs[webSelection].anchors[anchor0Selection].y,
                                                ttrpg.webs[webSelection].anchors[anchor1Selection].x,
                                                ttrpg.webs[webSelection].anchors[anchor1Selection].y
                                            ) + (anchor0Difference)
                                        )


                                    }

                                    if (advanced.not()) {
                                        Text("Difference: ${anchor1Difference.toInt()}")
                                        Slider(
                                            value = anchor1Difference.toFloat(),
                                            valueRange = anchor1MinDiff.toFloat()..anchor1MaxDiff.toFloat(),
                                            onValueChange = { anchor1Difference = it.toDouble() },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(25.dp, 10.dp)
                                        )
                                    } else {
                                        Box(
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            TextField(
                                                value = anchor1Difference.toInt()
                                                    .toString(),
                                                onValueChange = {
                                                    anchor0Difference = if (it.matches(Regex("^[0-9]+\$"))) {
                                                        it.toDouble()
                                                    } else {
                                                        0.5
                                                    }
                                                },
                                                label = { Text(text = "Anchor Name:") },
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // third Anchor

                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
                        ) {
                            Button(
                                content = {
                                    if (webSelection == -1) {
                                        Text("Select Web First!")
                                    } else if (anchor0Selection == -1) {
                                        Text("Select first anchor first")
                                    } else if (anchor1Selection == -1) {
                                        Text("Select second anchor first")
                                    } else if (anchor2Selection == -1) {
                                        Text("Third Anchor")
                                    } else {
                                        Text("Anchor: ${ttrpg.webs[webSelection].anchors[anchor2Selection].name}")
                                    }
                                },
                                onClick = { anchor2Expanded = true },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            )
                            if (webSelection != -1 && anchor0Selection != -1 && anchor1Selection != -1) {
                                DropdownMenu(
                                    onDismissRequest = { anchor2Expanded = false },
                                    expanded = anchor2Expanded
                                ) {
                                    ttrpg.webs[webSelection].anchors.forEachIndexed { anchorIndex, eachAnchor ->
                                        if ((anchor0Selection != anchorIndex) && (anchor1Selection != anchorIndex)) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    anchor2Selection = anchorIndex
                                                    anchor2Expanded = false
                                                }) {
                                                Text(eachAnchor.name)
                                            }
                                        }
                                    }
                                    Divider(startIndent = 1.dp, thickness = 1.dp, color = Color.Black)
                                    DropdownMenuItem(
                                        onClick = {
                                            anchor2Selection = -1
                                        }
                                    ) {
                                        Text("RESET")
                                    }
                                }
                            }
                        }
                        Box {
                            Button(
                                content = {
                                    if (webSelection == -1) {
                                        Text("Select Web First!")
                                    } else if (anchor0Selection == -1) {
                                        Text("Select first anchor first")
                                    } else if (anchor1Selection == -1) {
                                        Text("Select second anchor first")
                                    } else if (anchor2Selection == -1) {
                                        Text("Select third anchor first")
                                    } else if (optionSelected == -1) {
                                        Text("Difference")
                                    } else {
                                        Text("Difference:")
                                    }
                                },
                                onClick = { optionExpanded = true },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            )

                            if (anchor2Selection != -1) {
                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                ) {
                                    DropdownMenu(
                                        onDismissRequest = { optionExpanded = false },
                                        expanded = optionExpanded
                                    ) {
                                        DropdownMenuItem(
                                            onClick = {
                                                optionSelected = 0
                                                optionExpanded = false
                                            }
                                        ) {
                                            Text("smallest difference")
                                        }
                                        DropdownMenuItem(
                                            onClick = {
                                                optionSelected = 1
                                                optionExpanded = false
                                            }
                                        ) {
                                            Text("largest difference")
                                        }
                                        if (optionSelected != -1) {
                                            newAnchorCoords = intersect(
                                                ttrpg.webs[webSelection].anchors[anchor0Selection].x,
                                                ttrpg.webs[webSelection].anchors[anchor0Selection].y,
                                                anchor0Difference,
                                                ttrpg.webs[webSelection].anchors[anchor1Selection].x,
                                                ttrpg.webs[webSelection].anchors[anchor1Selection].y,
                                                anchor1Difference,
                                                ttrpg.webs[webSelection].anchors[anchor2Selection].x,
                                                ttrpg.webs[webSelection].anchors[anchor2Selection].y
                                            )[optionSelected]
                                        }
                                    }
                                }
                            }
                        }
                        Button(
                            content = { Text("Create Anchor!") },
                            onClick = { submit = true },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )

                        if (submit) {
                            ttrpg.webs[webSelection].anchors.add(
                                Anchor(
                                    newAnchorName,
                                    newAnchorCoords[0],
                                    newAnchorCoords[1]
                                )
                            )
                            isNewAnchorDialogOpen = false
                        }

                    }
                }
            }

            if (isNewCharacterDialogOpen) {
                var newCharacterName by remember { mutableStateOf("Example Character") }
                var submit by remember { mutableStateOf(false) }
                Dialog(
                    title = "New Character",
                    onCloseRequest = { isNewCharacterDialogOpen = false },
                    state = rememberDialogState(
                        position = WindowPosition(Alignment.Center),
                        600.dp, 700.dp
                    ),
                    resizable = true
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(25.dp)
                    ) {
                        TextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            value = newCharacterName,
                            onValueChange = { newCharacterName = it },
                            label = { Text(text = "Character Name:") },
                        )

                        Button(
                            onClick = { submit = true },
                            content = {
                                Text("Create Character")
                            }
                        )

                        if (submit) {
                            party.characters.add(Character(newCharacterName))
                        }
                    }
                }
            }

            if (isNewTTRPGDialogOpen) {
                Dialog(
                    title = "New System",
                    onCloseRequest = { isNewTTRPGDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    var load by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
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
                            isNewTTRPGDialogOpen = false
                            ttrpg = remember { TTRPG(systemName, authorName) }
                            isTTRPGLoaded = true
                            winTitle = "Forged: $systemName"
                        }

                    }
                }
            }

            if (isNewPartyDialogOpen) {
                var newPartyName by remember { mutableStateOf("Example Party") }
                var submit by remember { mutableStateOf(false) }
                Dialog(
                    title = "New Party",
                    onCloseRequest = { isNewPartyDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(25.dp)
                    ) {
                        TextField(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            value = newPartyName,
                            onValueChange = { newPartyName = it },
                            label = { Text(text = "Party Name:") },
                        )

                        Button(
                            onClick = { submit = true },
                            content = {
                                Text("Create Party")
                            }
                        )

                        if (submit) {
                            party = remember { Party(newPartyName) }
                            isPartyLoaded = true
                            isNewPartyDialogOpen = false
                        }
                    }
                }
            }

            if (isSelectAnchorsDialogOpen) {
                Dialog(
                    title = "Select anchors",
                    onCloseRequest = { isNewPartyDialogOpen = false },
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(25.dp)
                    ) {

                    }
                }
            }

            // Main App
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (isTTRPGLoaded) {
                    // Show Anchors in web
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(color = Color(180, 180, 180))
                            .width(250.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column {
                                Text("${ttrpg.name}:")
                                for ((webIndex, eachWeb) in ttrpg.webs.withIndex()) {
                                    Text(" $webIndex: ${eachWeb.name}")
                                    for ((anchorIndex, eachAnchor) in eachWeb.anchors.withIndex()) {
                                        Text(" - $anchorIndex: ${eachAnchor.name} (${eachAnchor.x.toInt()}, ${eachAnchor.y.toInt()})")
                                    }
                                }
                            }
                        }
                    }
                }

                if (isPartyLoaded) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column {
                                Button(
                                    onClick = { isSelectAnchorsDialogOpen = true },
                                    content = {
                                        Text("Select Anchors")
                                    }
                                )
                            }
                        }
                    }

                    // Show characters in party
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(color = Color(180, 180, 180))
                            .width(250.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column {
                                Text("Party: ${party.name}", textAlign = TextAlign.Start)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun intersect(
    firstX: Double, firstY: Double, firstR: Double,
    secondX: Double, secondY: Double, secondR: Double,
    anchorX: Double, anchorY: Double
): List<List<Double>> {
    val first = Circle(firstX, firstY, firstR)
    val second = Circle(secondX, secondY, secondR)
    var centerDistance = dist(first.x, first.y, second.x, second.y)
    while (
        centerDistance > first.r + second.r
        || centerDistance <= abs(first.r - second.r)
        || centerDistance == 0.0
        || centerDistance == first.r + second.r
    ) {
        for (i in 0..1) {
            first.randShift(i)
            second.randShift(i)
        }

        centerDistance = dist(first.x, first.y, second.x, second.y)
    }
    val chordDistance = ((first.r).pow(2) - (second.r).pow(2) + centerDistance.pow(2)) / (centerDistance * 2)
    val halfChordLength = ((first.r).pow(2) - chordDistance.pow(2)).pow(0.5)
    val chordMidpointX = first.x + (chordDistance * (second.x - first.x) / centerDistance)
    val chordMidpointY = first.y + (chordDistance * (second.y - first.y) / centerDistance)
    val option1 = mutableListOf(
        chordMidpointX + (halfChordLength * (second.y - first.y) / centerDistance),
        chordMidpointY - (halfChordLength * (second.x - first.x) / centerDistance),
    )

    val option2 = mutableListOf(
        chordMidpointX - (halfChordLength * (second.y - first.y) / centerDistance),
        chordMidpointY + (halfChordLength * (second.x - first.x) / centerDistance),
    )


    return if (dist(option1[0], option1[1], anchorX, anchorY) > dist(option2[0], option2[1], anchorX, anchorY)) {
        listOf(option2, option1)
    } else {
        listOf(option1, option2)
    }

}

class Circle(
    var x: Double,
    var y: Double,
    var r: Double
) {
    fun randShift(index: Int) {
        when (index) {
            0 -> x += Random.nextDouble(-1.0, 1.0)
            1 -> y += Random.nextDouble(-1.0, 1.0)
        }
    }
}

fun save(toSave: Any, saveName: String): Boolean {
    val gson = Gson()
    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    val jsonSaveListPretty: String = gsonPretty.toJson(toSave)
    File("$saveName.json").writeText(jsonSaveListPretty)
    return true
}

fun load(saveName: String): Any {
    val inputStream: InputStream = File(saveName).inputStream()
    val loadString = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    val result: JsonObject = Gson().fromJson(loadString, JsonObject::class.java)
    // var type = result.ge
    return if (result.get("type") != null) {
        if (result.get("type").asString == "System") {
            Gson().fromJson(loadString, TTRPG::class.java)
        } else {
            Gson().fromJson(loadString, Party::class.java)
        }
    } else {
        ""
    }
}


fun dist(x0: Double, y0: Double, x1: Double, y1: Double): Double {
    return (sqrt((x0 - x1).pow(2) + (y0 - y1).pow(2)))
}


class Character(
    var name: String,
    var anchors: SnapshotStateList<Anchor> = mutableStateListOf()
) {

}

class Party(
    var name: String,
    var characters: SnapshotStateList<Character> = mutableStateListOf()
) {
    val type = "Party"
}

open class Anchor(
    var name: String,
    val x: Double,
    val y: Double,
    var exp: Double = 0.0
) {
    fun character(xp: Double = 0.0) {

    }

    override fun toString(): String {
        return " $name: ($x, $y)\n  "
    }
}

class Web(
    var name: String,
    anchor0: String,
    anchor1: String,
    anchor2: String
) {
    var anchors = mutableStateListOf<Anchor>()

    init {
        anchors.add(Anchor(anchor0, 0.0, 433.013))
        anchors.add(Anchor(anchor1, 500.0, -433.013))
        anchors.add(Anchor(anchor2, -500.0, -433.013))
    }

    override fun toString(): String {
        val anchorOut = mutableListOf<Anchor>()
        anchors.forEach {
            anchorOut.add(it)
        }
        return " name: $name\n anchors:\n ${anchorOut.toString()}\n"
    }

    fun addAnchor(anchorName: String, anchorX: Double, anchorY: Double) {
        anchors.add(
            Anchor(
                anchorName,
                (anchorX + Random.nextDouble(-1.0, 1.0)),
                (anchorY + Random.nextDouble(-1.0, 1.0))
            )
        )
    }
}

class TTRPG(
    var name: String,
    var author: String,
    var webs: SnapshotStateList<Web> = mutableStateListOf(),
    val created: Long = System.currentTimeMillis()
) {
    val type = "System"

    override fun toString(): String {
        val webOut = mutableListOf<Web>()
        webs.forEach {
            webOut.add(it)
        }
        return " name: $name\n author: $author\n Webs:\n $webOut\n"
    }

}

