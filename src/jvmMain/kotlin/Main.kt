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
import androidx.compose.material.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberDialogState
import androidx.compose.ui.window.rememberWindowState
import java.awt.GraphicsEnvironment
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
    var ttrpg by remember { mutableStateOf(TTRPG("", "")) }
    Window(
        title = winTitle,
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            position = WindowPosition(Alignment.CenterStart),
            height = (DISPLAYHEIGHT - 50).dp, width = (DISPLAYWIDTH - 50).dp
        )
    ) {
        MaterialTheme {
            var isNewTTRPGDialogOpen by remember { mutableStateOf(false) }
            var isCopyrightDialogOpen by remember { mutableStateOf(false) }
            var isNewWebDialogOpen by remember { mutableStateOf(false) }
            var isNewAnchorDialogOpen by remember { mutableStateOf(false) }
            var notImplemented by remember { mutableStateOf(false) }
            var isLoaded by remember { mutableStateOf(false) }
            var systemName by remember { mutableStateOf("Example System") }
            var authorName by remember { mutableStateOf("Example Author") }

            MenuBar {

                // Menu Bar Items
                Menu("File") {
                    Item("New", onClick = { isNewTTRPGDialogOpen = true })
                    Item("Load", onClick = { notImplemented = true }) // TODO: Add loading functionality
                    if (isLoaded) {
                        Item("Save", onClick = { notImplemented = true })
                    }
                    Item("Swap Theme", onClick = { DarkTheme = DarkTheme.not() })
                    Item("Exit", onClick = { exitApplication() })
                }

                if (isLoaded) {
                    Menu("Edit") {
                        Item("Create Web", onClick = { isNewWebDialogOpen = true })
                        if (ttrpg.webs.size != 0) {
                            Item("Create Anchor", onClick = { isNewAnchorDialogOpen = true })
                        }
                    }
                }

                Menu("Help") {
                    Item("Manual", onClick = { notImplemented = true }) // TODO: Add manual link
                    Item("Copyright", onClick = { isCopyrightDialogOpen = true })
                }

                // Dialog Boxes
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
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(25.dp, 5.dp, 25.dp, 5.dp)
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
                            600.dp, 550.dp
                        ),
                        resizable = true,
                    ) {
                        var anchorName by remember { mutableStateOf("Example Anchor") }

                        var webExpanded by remember { mutableStateOf(false) }
                        var webSelection by remember { mutableStateOf(-1) }

                        var anchor0Selection by remember { mutableStateOf(-1) }
                        var anchor0Difference by remember { mutableStateOf(0.5) }
                        var anchor0Expanded by remember { mutableStateOf(false) }

                        var anchor1Selection by remember { mutableStateOf(-1) }
                        var anchor1Difference by remember { mutableStateOf(0.5) }
                        var anchor1Expanded by remember { mutableStateOf(false) }
                        var anchor1MaxDiff by remember { mutableStateOf(2000.0) }
                        var anchor1MinDiff by remember { mutableStateOf(0.0) }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(25.dp)
                        ) {
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
                                value = anchorName,
                                onValueChange = { anchorName = it },
                                label = { Text(text = "Anchor Name:") },
                            )

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
                                            Text("Anchor: ${ttrpg.webs[anchor0Selection].name}")
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
                                            DropdownMenuItem(
                                                onClick = {
                                                    anchor0Selection = anchorIndex
                                                    anchor0Expanded = false
                                                }) {
                                                Text(eachAnchor.name)
                                            }
                                        }
                                    }
                                }
                            }

                            Box(
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
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
                                            Text("Difference: ${(anchor0Difference * 2000).toInt()}")
                                            Slider(
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
                                                    value = (anchor0Difference * 2000).toInt().toString(),
                                                    onValueChange = {
                                                        anchor0Difference = if (it.matches(Regex("^[0-9]+\$"))) {
                                                            it.toDouble() / 2000
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

                            Box(
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Button(
                                    content = {
                                        if (anchor1Selection == -1) {
                                            Text("Select Second Anchor")
                                        } else {
                                            Text("Anchor: ${ttrpg.webs[webSelection].anchors[anchor1Selection].name}")
                                        }
                                    },
                                    onClick = { anchor1Expanded = true },
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                )
                                DropdownMenu(
                                    onDismissRequest = { anchor1Expanded = false },
                                    expanded = anchor1Expanded
                                ) {
                                    ttrpg.webs[webSelection].anchors.forEachIndexed { anchorIndex, eachAnchor ->
                                        var passed = false
                                        if (anchorIndex != anchor0Selection) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    anchor1Selection = if (passed) {
                                                        anchorIndex
                                                    } else {
                                                        anchorIndex + 1
                                                    }
                                                    anchor1Expanded = false
                                                }) {
                                                Text(eachAnchor.name)
                                            }
                                        } else {
                                            passed = true
                                        }
                                    }
                                }
                            }

                            Box(
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
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
                                        if (anchor1Selection != -1) { // TODO: Find a way to have these values set correctly
                                            anchor1MinDiff = abs(
                                                dist(
                                                    ttrpg.webs[webSelection].anchors[anchor0Selection].x,
                                                    ttrpg.webs[webSelection].anchors[anchor0Selection].y,
                                                    ttrpg.webs[webSelection].anchors[anchor1Selection].x,
                                                    ttrpg.webs[webSelection].anchors[anchor1Selection].y
                                                ) - (anchor0Difference * 2000)
                                            )
                                            anchor1MaxDiff = abs(
                                                dist(
                                                    ttrpg.webs[webSelection].anchors[anchor0Selection].x,
                                                    ttrpg.webs[webSelection].anchors[anchor0Selection].y,
                                                    ttrpg.webs[webSelection].anchors[anchor1Selection].x,
                                                    ttrpg.webs[webSelection].anchors[anchor1Selection].y
                                                ) + (anchor0Difference * 2000)
                                            )
                                        }

                                        if (advanced.not()) {
                                            Text("Difference: ${((anchor1Difference * abs(anchor1MaxDiff - anchor1MinDiff)) + anchor1MinDiff).toInt()}")
                                            Slider(
                                                value = anchor1Difference.toFloat(),
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
                                                    value = ((anchor0Difference * abs(anchor1MaxDiff - anchor1MinDiff)) + anchor1MinDiff).toInt()
                                                        .toString(),
                                                    onValueChange = {
                                                        anchor0Difference = if (it.matches(Regex("^[0-9]+\$"))) {
                                                            it.toDouble() / 2000
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
                                isLoaded = true
                                winTitle = "Forged: ${systemName}"
                            }

                        }
                    }
                }
            }

            // Main App
            if (isLoaded) {
                MaterialTheme {
                    ttrpg = remember { TTRPG(systemName, authorName) }
                    // Show Anchors in web

                    // if (ttrpg.webs.size != 0 and ttrpg.webs.size) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(color = Color(180, 180, 180))
                    ) {
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
    }
}

fun dist(x0: Double, y0: Double, x1: Double, y1: Double): Double {
    return (sqrt((x0 - x1).pow(2) + (y0 - y1).pow(2)))
}

class Anchor(
    var name: String,
    val x: Double,
    val y: Double,
) {
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

    override fun toString(): String {
        val webOut = mutableListOf<Web>()
        webs.forEach {
            webOut.add(it)
        }
        return " name: $name\n author: $author\n Webs:\n $webOut\n"
    }

}

