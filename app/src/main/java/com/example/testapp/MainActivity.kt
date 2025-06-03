package com.example.testapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.ui.theme.TestAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

val firestore = Firebase.firestore


class Usuario {
    var nome = ""
    var endereco = ""
    var bairro = ""
    var cep = ""
    var cidade = ""
    var estado = ""
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPreview()
        }
    }
}



@Composable
fun App() {
    val usuarios = remember { mutableStateListOf<Usuario>() }

    val usuariosRef = firestore.collection("users")

    usuariosRef.addSnapshotListener { snapshots, e ->
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshots != null && !snapshots.isEmpty) {
            usuarios.clear() 

            for (document in snapshots) {
                val usuario = Usuario().apply {
                    nome = document.getString("nome") ?: ""
                    endereco = document.getString("endereco") ?: ""
                    bairro = document.getString("bairro") ?: ""
                    cep = document.getString("cep") ?: ""
                    cidade = document.getString("cidade") ?: ""
                    estado = document.getString("estado") ?: ""
                }
                usuarios.add(usuario)
            }

            Log.d(TAG, "Usuários atualizados: ${usuarios.size}")
        } else {
            Log.d(TAG, "Nenhum dado encontrado.")
            usuarios.clear()
        }
    }


    var nome by remember{
        mutableStateOf("")
    }
    var endereco by remember{
        mutableStateOf("")
    }
    var bairro by remember{
        mutableStateOf("")
    }
    var cep by remember{
        mutableStateOf("")
    }
    var cidade by remember{
        mutableStateOf("")
    }
    var estado by remember{
        mutableStateOf("")
    }
    val user = hashMapOf(
        "nome" to nome,
        "endereco" to endereco,
        "bairro" to bairro,
        "cep" to cep,
        "cidade" to cidade,
        "estado" to estado,
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "App Agendamento",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            color = androidx.compose.ui.graphics.Color(0xFF1976D2)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .then(Modifier),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = endereco,
                onValueChange = { endereco = it },
                label = { Text("Endereço") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = bairro,
                onValueChange = { bairro = it },
                label = { Text("Bairro") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cep,
                onValueChange = { cep = it },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    firestore.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Cadastrar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = "Usuários cadastrados",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            usuarios.forEach { usuario ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFF5F5F5)
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nome: ${usuario.nome}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Endereço: ${usuario.endereco}")
                        Text(text = "Bairro: ${usuario.bairro}")
                        Text(text = "CEP: ${usuario.cep}")
                        Text(text = "Cidade: ${usuario.cidade}")
                        Text(text = "Estado: ${usuario.estado}")
                    }
                }
            }
        }
    }
}


    @Preview(showBackground = true)
    @Composable
    fun AppPreview() {
        TestAppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                App()
            }
        }
    }