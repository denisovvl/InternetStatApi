package com.example.network.statistic.db

import com.example.network.statistic.domian.CheckUserIsExistUseCase
import com.example.network.statistic.domian.app.AddAppUseCase
import com.example.network.statistic.domian.app.GetAppUseCase
import com.example.network.statistic.domian.networdata.AddNetworkDataUseCase
import com.example.network.statistic.domian.networdata.GetNetworkDataUseCase
import com.example.network.statistic.domian.user.AddUserUseCase
import com.example.network.statistic.domian.user.GetUsersUseCase
import com.example.network.statistic.models.Application
import com.example.network.statistic.models.NetworkData
import com.example.network.statistic.models.NetworkPeriod
import com.example.network.statistic.models.UserApplicationResponse
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class DbHelper {

    init {
        Database.connect(
            "jdbc:postgresql://shefer.space/vdenisov_diplom", driver = "org.postgresql.Driver",
            user = "vdenisov_diplom", password = "Js!t7B8AG#CM5v9&"
        )
        transaction {
            SchemaUtils.create(
                Db.NetworkData,
                Db.Users,
                Db.UserApplications
            )
            SchemaUtils.createMissingTablesAndColumns(
                Db.NetworkData,
                Db.Users,
                Db.UserApplications
            )
        }
    }

    fun addUser(user: String): Boolean = AddUserUseCase(user).execute()

    fun getUsers(): ArrayList<String> = GetUsersUseCase().execute()

    fun addUserApps(userApps: UserApplicationResponse) {
        CheckUserIsExistUseCase(userApps.user).execute()
        AddAppUseCase(userApps).execute()
    }

    fun getUserApps(user: String): ArrayList<Application> {
        CheckUserIsExistUseCase(user).execute()
        return GetAppUseCase(user).execute()
    }

    fun addNetworkData(user: String, networkData: ArrayList<NetworkData>) {
        CheckUserIsExistUseCase(user).execute()
        networkData.forEach {
            AddNetworkDataUseCase(it).execute()
        }
    }

    fun getNetworkData(
        user: String,
        period: NetworkPeriod,
        startTime: Long,
        endTime: Long
    ): ArrayList<NetworkData> {
        CheckUserIsExistUseCase(user).execute()
        return GetNetworkDataUseCase(user, period, startTime, endTime).execute()
    }
}