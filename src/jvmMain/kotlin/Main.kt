/*
 *     Copyright (c) 2023 Jade Neoma <Joung3010@gmail.com>
 *
 *
 *     This file is part of Forged.
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
import androidx.compose.ui.input.key.Key.Companion.Menu
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import javax.swing.GroupLayout
import javax.swing.text.Position
import kotlin.system.exitProcess

@Composable
@Preview
fun App() {

}

@Composable
@Preview
fun NewSystemDialog(){
    MaterialTheme {
        var systemName by remember { mutableStateOf("Example System") }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(50.dp)
        ){
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = systemName,
                onValueChange = { systemName = it },
                label = { Text(text = "System Name") },
                )

            Button(
                content = {Text("Create System!")},
                onClick = {createNew(systemName)},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 50.dp, 0.dp, 0.dp)
            )
        }
    }
}

fun createNew(systemName:String){
    print(systemName)
}

fun main() = application {
    Window(

        onCloseRequest = ::exitApplication
    ) {

        var isNewDialogOpen by remember { mutableStateOf(false) }
        var isLicenceDialogOpen by remember { mutableStateOf(false) }

        MenuBar {
            Menu("File"){
                Item("New", onClick = { isNewDialogOpen = true })
                Item("Load", onClick = {}) // TODO: Add loading functionality
                Item("Exit", onClick = {exitApplication()})
            }

            Menu("Help"){
                Item("Manual", onClick = {})
                Item("License", onClick = {isLicenceDialogOpen = true}) // TODO: Add loading functionality
            }

            if (isLicenceDialogOpen) {
                Dialog(
                    title = "License",
                    onCloseRequest = {isLicenceDialogOpen = false},
                    state = rememberDialogState(position = WindowPosition(Alignment.Center))
                ){

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
        App()
    }
}
