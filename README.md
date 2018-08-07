# SampleProject
This is a sample repository for NY times API

The code follows MVC pattern - 

<br><strong>MainActivity</strong> - This is the laucher activity which interacts with NY times API Using the help of RequestController for the first time.</br>
<br><strong>DetailActivity</strong> - The second activity which opens on click of any of the row of the MainActivity list.</br>
<br><strong>RequestController</strong> - Helps in interacting with the NetWork, creates a connection and returns response in form of byte[] to RequestController.</br>
<br><strong>JsonParser</strong> - This class helps in parsing the responser received from the API and send it back to the UIThread of the MainActivity with the help of RequestController.</br>
<br><strong>Models</strong> - There are three models used in this project. <strong>ResponseModel(The Pojo class for getting "results array objects" from response), MediaModel(Pojo class for "Media array objects"), MediaMetadata(Pojo class for "media-metadata")</strong></br>
<br><strong>Recycleriew</strong> - It is used for displaying the list in MainActivity with the help of <strong>CardView</strong></br>
