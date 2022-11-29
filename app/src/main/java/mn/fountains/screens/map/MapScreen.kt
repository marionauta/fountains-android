package mn.fountains.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mn.fountains.domain.models.Fountain
import mn.fountains.domain.repositories.FountainRepository
import java.net.URL

@Composable
fun MapScreen(url: URL, navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(url.toString()) })
    }) {
        Box(modifier = Modifier.padding(it)) {
            Map(url = url)
        }
    }
}

@Composable
private fun Map(url: URL) {
    val (fountains, setFountains) = remember { mutableStateOf(listOf<Fountain>()) }
    val namedFountains = fountains.filter { it.name.isNotBlank() }

    LaunchedEffect(url) {
        val repository = FountainRepository()
        setFountains(repository.all(url = url) ?: listOf())
    }

    LazyColumn {
        itemsIndexed(namedFountains) { index, fountain ->
            if (index > 0) {
                Divider()
            }
            Text(fountain.name, modifier = Modifier.padding(16.dp))
        }
    }
}
