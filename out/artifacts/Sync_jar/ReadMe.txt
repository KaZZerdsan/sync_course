Параметр для environment.properties - -DenvironmentPath
Параметр для логгера - log4j2.configurationFile

Аргументы для добавления записей:

createAdmin - Создание Администатора Bool append, Long id, String name
createManager - Создание Менеджера Bool append, Long id, String name
createSpeaker - Создание Спикера Bool append, Long id, String name
createChannel - Создание Канала Bool append, Long id, String name, Bool status, String language, Long speakerIds (Может быть сколько угодно)
createZone - Создание Зала Bool append, Long id, String name, int dateStart, int dateEnd, Long channelIds (Может быть сколько угодно)
createEvent - Создание Мероприятия Bool append, Long id, String name, Long managerId, Long zoneIds (Может быть сколько угодно)

Получение записей:

getAdmins
getManagers
getSpeakers
getChannels
getZones
getEvents

Обновление записей

updateAdmin - Обнолвение Администатора Long id, String name
updateManager - Обновление Менеджера Long id, String name
updateSpeaker - Обновление Спикера Long id, String name
updateChannel - Обновление Канала Long id, String name, Bool status, String language, Long speakerIds (Может быть сколько угодно)
updateZone - Обновление Зала Long id, String name, int dateStart, int dateEnd, Long channelIds (Может быть сколько угодно)
updateEvent - Обновление Мероприятия Long id, String name, Long managerId, Long zoneIds (Может быть сколько угодно)

Удаление записей

Во всех случаях необходимо указать только Long id!

deleteAdmin
deleteManager
deleteSpeaker
deleteChannel
deleteZone
deleteEvent

Изменение статуса зала

changeZoneStatus Long id, Boolean Status

Типы Датасорсов:
Csv
XML
JAXB
DB

Примеры запуска:
java -jar -DenvironmentPath=./environment.properties -Dlog4j2.configurationFile=./log4j2.properties ./Sync.jar db getAdmins
java -jar -DenvironmentPath=./environment.properties -Dlog4j2.configurationFile=./log4j2.properties ./Sync.jar db createSpeaker true 25 "Anatoly"
java -jar -DenvironmentPath=./environment.properties -Dlog4j2.configurationFile=./log4j2.properties ./Sync.jar db deleteSpeaker 25

