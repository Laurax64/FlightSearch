/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.flightsearch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.flightsearch.data.Airport

/**
 * Displays the Flight Search app screen
 */
@Composable
fun FlightSearchApp(

) {

}

/**
 * Displays the Flight Search app search bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    content: @Composable (ColumnScope.() -> Unit),
    modifier: Modifier = Modifier
) {
SearchBar(query, onQueryChange, onSearch, active, onActiveChange, modifier) {
    content
}
}

/**
 * Displays a card for the given [Airport]
 */
@Composable
private fun AirportCard(airport: Airport){
    Card(colors = CardDefaults.cardColors(contentColor = Color(0xff1d1b20))) {
        Column {
            Text(text = airport.iataCode, style = MaterialTheme.typography.bodyLarge)
            Text(text = airport.name)
        }
    }
}

/**
 * Displays a card containing a route between the given [Airport]s and a radio button
 * that lets the user add or remove the route from the favorite routes
 */
@Composable
private fun RouteCard(destination: Airport, arrival: Airport, favorite: Boolean) {
    Card(colors = CardDefaults.cardColors(contentColor = Color(0xff1d1b20))) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(weight = 1f)
            ) {
                Text(text = "Depart", style = MaterialTheme.typography.labelMedium)
                Text(text = destination.iataCode)
                Text(text = destination.name)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(weight = 1f)
            ) {
                Text(text = "Arrive", style = MaterialTheme.typography.labelMedium)
                Text(text = arrival.iataCode)
                Text(text = arrival.name)
            }
            IconButton(onClick = { /*TODO*/ }) {
                if(favorite) { Icons.Filled.Star }
                else { Icons.Outlined.Star }
            }
        }
    }
}


/**
 * Displays the Flight Search Apps Top Bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FsTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Flight search",
                color = Color(0xff1d1b20),
                textAlign = TextAlign.Center,
                lineHeight = 1.27.em,
                style = MaterialTheme.typography.titleLarge)
        },
        modifier = modifier)
}

@Composable
fun FavoriteRoutes(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .background(color = Color(0xfffef7ff))
    ) {
        // TODO
        }
    }

