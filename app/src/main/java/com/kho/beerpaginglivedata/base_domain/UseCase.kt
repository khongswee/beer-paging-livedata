package com.kho.beerpaginglivedata.base_domain

import com.kho.beerpaginglivedata.base_data.Either

/**
 * Created by Administrator on 9/20/18.
 */
abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<NetworkState, Type>

    operator fun invoke(params: Params, onResult: (Either<NetworkState, Type>) -> Unit = {}) {

    }

    class None
}
