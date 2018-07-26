package com.kho.beerpaginglivedata.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class BeerModel(
        @SerializedName("status") val status: Int,
        @SerializedName("message") val message: String,
        @SerializedName("pager") val pager: Pager,
        @SerializedName("result") val result: List<BeerResult>
)

@Entity
data class BeerResult(
        @PrimaryKey @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("tags") val tags: String,
        @SerializedName("price_in_cents") val priceInCents: Int,
        @NonNull @SerializedName("image_thumb_url") val imageThumbUrl: String?="",
        @NonNull @SerializedName("image_url") val imageUrl: String?=""
)


data class Pager(
        @SerializedName("records_per_page") var recordsPerPage: Int=0,
        @SerializedName("total_record_count") var totalRecordCount: Int=0,
        @SerializedName("current_page_record_count") var currentPageRecordCount: Int=0,
        @SerializedName("is_first_page") var isFirstPage: Boolean=false,
        @SerializedName("is_final_page") var isFinalPage: Boolean=false,
        @SerializedName("current_page") var currentPage: Int=0,
        @SerializedName("current_page_path") var currentPagePath: String="",
        @SerializedName("next_page") var nextPage: Int=0,
        @SerializedName("next_page_path") var nextPagePath: String="",
        @SerializedName("previous_page") var previousPage: Int=0,
        @SerializedName("previous_page_path") var previousPagePath: String="",
        @SerializedName("total_pages") var totalPages: Int=0,
        @SerializedName("total_pages_path") var totalPagesPath: String=""
)
