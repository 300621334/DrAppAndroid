package comp231.drbooking;


import android.content.Context;

//Callback interface
// https://stackoverflow.com/questions/16800711/passing-function-as-a-parameter-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public interface ICallBackFromDbAdapter
{
    void onResponseFromServer(String result, Context ctx);
}
