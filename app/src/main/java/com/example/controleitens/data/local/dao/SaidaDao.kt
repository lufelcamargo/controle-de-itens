package com.example.controleitens.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.controleitens.data.local.entity.SaidaEntity

@Dao
interface SaidaDao {

    @Insert
    suspend fun inserir(saida: SaidaEntity)

    @Update
    suspend fun atualizar(saida: SaidaEntity)

    @Delete
    suspend fun excluir(saida: SaidaEntity)

    @Query("SELECT * FROM saidas WHERE id = :id")
    suspend fun buscarPorId(id: String): SaidaEntity?

    @Query("SELECT * FROM saidas")
    suspend fun buscarTodas(): List<SaidaEntity>
}