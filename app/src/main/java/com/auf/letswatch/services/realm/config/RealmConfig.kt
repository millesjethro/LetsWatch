package com.auf.letswatch.services.realm.config

import io.realm.RealmConfiguration

private const val realmVersion = 1L

object RealmConfig {
    fun getConfiguration(): RealmConfiguration{
        return RealmConfiguration.Builder()
            .schemaVersion(realmVersion)
            //.deleteRealmIfMigrationNeeded()
            .build()
    }
}