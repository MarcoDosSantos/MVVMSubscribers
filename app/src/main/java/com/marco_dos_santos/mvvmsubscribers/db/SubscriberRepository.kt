package com.marco_dos_santos.mvvmsubscribers.db

class SubscriberRepository(private val dao: SubscriberDao) {
/*Si hubiera otra fuente adicional de datos, como Retrofit, habría que pasar dos parámetros: private val alumnoRoomDao : AlunmoRoomDao y
* private val alumnoRetrofitDao: AlumnoRetrofit Dao*/

    /*Siguiendo el caso anterior, habría que intentar obtener primero la lista desde Retrofit. Si no se puede, entonces
    * la obtenemos de Room. Sería algo así:
    * fun getAllSubscribers(): List<Subscriber>{
    * private var SUBSCRIBERS? = subscribersRetrofitDao.getAllSubscribers(()
    *   if(SUBSCRIBERS == null){
    *       SUBSCRIBERS = alumnoRoomDao.getAllSubscribers()
    *   }
    * }
    * En esta aplicación, como es simple y no hay Retrofit, no tenemos necesidad de hacer la elección*/
    val subscribers = dao.getAllSubscribers()

    /* En caso de que hubiera Retrofit y Room, sería algo así:
    suspend fun insert(subscriber: Subscriber){
    try{
        alumnoRetrofitDao.insertSubscriber(subscriber)
        } catch(exception: Exception){
            Log.d("Retrofit exception", exception.toString()
            } finally{
            alumnoRoomDao.insertSubscriber(subscriber)//Aunque puede no haber memoria o batería suficiente: hay que ver.
            }
    }
    Probablemente no funcione y haya que mejorarlo, pero la idea es esa.
    * */
    suspend fun insert(subscriber: Subscriber){
        dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber){
        dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber){
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

}