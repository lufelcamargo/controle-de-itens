package com.example.controleitens.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.controleitens.data.local.entity.ItemSaidaEntity

@Dao
interface ItemSaidaDao {

    @Insert
    suspend fun inserir(item: ItemSaidaEntity)

    @Update
    suspend fun atualizar(item: ItemSaidaEntity)

    @Delete
    suspend fun excluir(item: ItemSaidaEntity)

    @Query("SELECT * FROM itens_saida WHERE id = :id")
    suspend fun buscarPorId(id: String): ItemSaidaEntity?

    @Query("SELECT * FROM itens_saida WHERE saidaId = :saidaId")
    suspend fun buscarPorSaidaId(saidaId: String): List<ItemSaidaEntity>
}