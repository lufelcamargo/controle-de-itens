package com.example.controleitens.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.controleitens.data.local.entity.ModeloEntity

@Dao
interface ModeloDao {

    @Insert
    suspend fun inserir(modelo: ModeloEntity)

    @Update
    suspend fun atualizar(modelo: ModeloEntity)

    @Delete
    suspend fun excluir(modelo: ModeloEntity)

    @Query("SELECT * FROM modelos WHERE id = :id")
    suspend fun buscarPorId(id: String): ModeloEntity?

    @Query("SELECT * FROM modelos")
    suspend fun buscarTodas(): List<ModeloEntity>
}