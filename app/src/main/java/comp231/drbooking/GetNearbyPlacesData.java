package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Display nearby hospitals in Map.
 * It's AsyncTask & uses DownloadUri class to send HTTP request. And uses DataParser to parse JSON response from that call.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>//args,progress,result
{
    //Class Variables
    String googlePlacesData, url;
    GoogleMap mMap;
    Context ctx;

    public GetNearbyPlacesData(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Object... objects)
    {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try
        {
            googlePlacesData = downloadUrl.readUrl(url);//get JSON string
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    /*//Sample response from Google nearby places
    {
   "html_attributions" : [],
   "next_page_token" : "CrQCIQEAACH3b3SE5EQQWPwiBYTqiWvp96xkPaMC4rFHDGvO63ZfEei7RhPyHY0ux6uHz2vhg8AqtZr8fP6DgHuyL1CnOTn5KFFMlCQoau0m4GnCHoDqVUhEGfciXfpjmsLHKj6hpr1BesiJx_y5k94XPr3QT3khoxafDOhy8ZflQFBcyVJyG_458uUXZq1hmTwsV2XOohdqKFwHgUPbs1sOHIOnAjHE8eByuf-FfSdazQvlYRH3be8zz_7dLoI2rUJorcZMa-_Mgy5yWVxgftkU-eVMQ8PT1DSedEW_gJKMvk_uS3uQ4byqG8t3Mdpyke1aWhJ2Iv9x9xrRjUP4W3DT1JGbxNnIwRlQnIl66Mz0M2o0_ra4Q4qQTSFp3Sp6TMP6t_YlcD_iA9I9xQG-aMjBhvIkAg0SEG6TyoRrnaH5ybFS_km7iSwaFC6u9cNRYUZjGkaFyJF6XVyJykFY",
   "results" : [
      {
         "geometry" : {
            "location" : {
               "lat" : 43.795553,
               "lng" : -79.239705
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.79690198029149,
                  "lng" : -79.23835601970849
               },
               "southwest" : {
                  "lat" : 43.79420401970849,
                  "lng" : -79.24105398029151
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "37864e43df0a7f0e7dfc637511c0b8d56a3a9c18",
         "name" : "Kitab Academy",
         "place_id" : "ChIJo41CyuDQ1IkRSFiY9iFPAWg",
         "plus_code" : {
            "compound_code" : "QQW6+64 Toronto, Ontario, Canada",
            "global_code" : "87M2QQW6+64"
         },
         "reference" : "CmRRAAAA7TUiODMNMbCDzCDxcjjKN5PvTr-3PKj0ZqkOxzSvIPZdSJe4YgjEryYGja7wijrTEgZoA_p7W8qYesHeAG0Sjm6CI2JEJNawSbwSf7THMGv9Udc3hRRoGY19_VP-rM3JEhCRYU_kFe0_xKChdX6T_po1GhRB3H33vpn_zczWq-vON8q3I4TVig",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "Toronto"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7887165,
               "lng" : -79.2357239
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7901288802915,
                  "lng" : -79.23406576970849
               },
               "southwest" : {
                  "lat" : 43.7874309197085,
                  "lng" : -79.2367637302915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png",
         "id" : "b900aafd7ccbb5ec70de8885a2e0434c53aca1fa",
         "name" : "Wynn Fitness Clubs (East)",
         "opening_hours" : {
            "open_now" : true
         },
         "photos" : [
            {
               "height" : 2268,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/109262063072225106954/photos\"\u003eWynn Fitness Clubs (East)\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAT2yBErZuhIa3HpSZFrO77dE5Bm-gO1WBFf92a1IhF_UAEs--fBU90w_Ie-2JOxJifbCOolOuOiwywK7Ruq7spTDBUBRx02u5KG28RAavKOKzCSM1s2bufqeh-6V6xkO4EhBacGjixXer3KPXc0fUu1n1GhRVPXRfVDwjPg0P7CzvR_pzALk8bQ",
               "width" : 4032
            }
         ],
         "place_id" : "ChIJkdo8DuXQ1IkRh5KLfoYmSts",
         "plus_code" : {
            "compound_code" : "QQQ7+FP Toronto, Ontario, Canada",
            "global_code" : "87M2QQQ7+FP"
         },
         "rating" : 3.9,
         "reference" : "CmRSAAAAO78ZmyWRgQVNQyxW2dgPZBVBzlPFmR3VzplzaT0q8y1lzaP3Hvxc4EfjarYMzE_BRiMX9iXhvFL3ImMXWRiEWMGoTToe4x3Hx9_LnttZb3VYeMVnWFbZq1XUXjAfM26yEhCsHNG_eiKTtuWA8o0oR1rbGhQO3B4vwu1IZE29wMi5iSoaFH84_g",
         "scope" : "GOOGLE",
         "types" : [ "gym", "health", "school", "point_of_interest", "establishment" ],
         "vicinity" : "10 Milner Business Court, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7781108,
               "lng" : -79.2280212
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.77907663029151,
                  "lng" : -79.2265088197085
               },
               "southwest" : {
                  "lat" : 43.77637866970851,
                  "lng" : -79.22920678029151
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "6b17696cd0c0f19aefdb5baa579c8095b8a3622a",
         "name" : "Woburn Collegiate Institute",
         "photos" : [
            {
               "height" : 800,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/114842505477372159901/photos\"\u003eChen Shaowei\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAFtoCijCOZgSAJmwg0PwWkUVa8qiTqQ6PoqgxhrHx1Y8EGVPjNYb3TAEVr0DTmyyK9QNyvI2aU3Dj_0DuwS7JQmMnKWaotnwsR51tSy9FXRT5uO4obUTR5CB1a9zyqfJkEhAR4bFdVcbEVOv35KrzBA9rGhQupS49pulDeMuRHTIdwDK-kAJKaQ",
               "width" : 1080
            }
         ],
         "place_id" : "ChIJd_2o7PXQ1IkR4hNL8b1plEg",
         "plus_code" : {
            "compound_code" : "QQHC+6Q Toronto, Ontario, Canada",
            "global_code" : "87M2QQHC+6Q"
         },
         "rating" : 3.6,
         "reference" : "CmRRAAAA2EitUkA1x5MJaE95IZTMFvBKJ8dAqUVGHXBh_S5OxVwLjuj_LaL1NDER3MffEiTsy6_4y1m8CWN23YuDYgJQ7Qomfvud98DkYS_cqKb63Zr1Ha-3DqhAvrL0edQJfyDkEhBoc5oBORBcMsF8oNAKFoH-GhTxGA270varbiPMIATxl3suMklslg",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "2222 Ellesmere Road, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7942657,
               "lng" : -79.2368862
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7956431302915,
                  "lng" : -79.2355486197085
               },
               "southwest" : {
                  "lat" : 43.7929451697085,
                  "lng" : -79.2382465802915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "1ad0dc21be15eb5894c452a1c9b47534e88e7d50",
         "name" : "A Graydon Hall Nursery Schools",
         "opening_hours" : {
            "open_now" : false
         },
         "place_id" : "ChIJ9V1DqufQ1IkRfAQf0WxIi0U",
         "plus_code" : {
            "compound_code" : "QQV7+P6 Toronto, Ontario, Canada",
            "global_code" : "87M2QQV7+P6"
         },
         "rating" : 5,
         "reference" : "CmRRAAAA3X2Ktsv3-pROJfVqUxT63MbVehFJRNmWOhHtebxUZ0WZe4-bUd3QMPuDPsx3TFqCe2BHk45SIXH_hIzvbsK-30_gPNEAoLNiDuClIjFZkFmVZhWOT7Kjg9CwBqOE-Ti9EhAgOoUPgjXs_AzbBxSDmVrvGhTSFcPQOq3MnVhffQNLlw3PdufTug",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "5600 Sheppard Avenue East, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7800062,
               "lng" : -79.2432441
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.78145893029149,
                  "lng" : -79.24193776970849
               },
               "southwest" : {
                  "lat" : 43.7787609697085,
                  "lng" : -79.24463573029151
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "a8f512906cf5d22a96c8db08e5d7a07987428cf7",
         "name" : "Three Fishes Christian Elementary School",
         "place_id" : "ChIJAdLTW1TQ1IkRKrgEtyJADr4",
         "plus_code" : {
            "compound_code" : "QQJ4+2P Toronto, Ontario, Canada",
            "global_code" : "87M2QQJ4+2P"
         },
         "rating" : 3.7,
         "reference" : "CmRSAAAApSOuUQe5oFNnlbPSlP51mxnFju7ORTeTzesQCKtLVWX0xooOxaGPj2nG6_mPoI9SViEpkT8Z2XpzIIwyvEJXRAwzMk53OcZa89H6oyJLI2l35mPLIFJTBIo9lq7xM-NqEhBGlVedlHSqKr0u91DctnTpGhR47ripveDV6mRuGa3ZRpDBUiO1Ew",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "801 Progress Avenue, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.776113,
               "lng" : -79.242251
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.77745418029149,
                  "lng" : -79.24093776970851
               },
               "southwest" : {
                  "lat" : 43.77475621970849,
                  "lng" : -79.24363573029152
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "72eab478c90d5f452ceebdb23e59b0a2fb1352ad",
         "name" : "TUSGU Educational Services - Bellamy & Ellesmere (Scarborough)",
         "opening_hours" : {
            "open_now" : true
         },
         "photos" : [
            {
               "height" : 3264,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/115717065919363354593/photos\"\u003eYousuf Khan\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAKSw738lnUH8QTcVXbmnKckiLXXbgoo6a08qLtSVy4mDuIB7JH2LV3RKLeYmaEgikU9aTeHDxe_C8h5vFZuJvZhrSaI4nj1rqKzM_uHLwLnG1P64nNFUYZGwmVNR3F9MYEhALY_RRWsMIFzdXXxUmBmKTGhT9c8BXFm38q9LZ1yBVqUR6k_5-7A",
               "width" : 1836
            }
         ],
         "place_id" : "ChIJgc3TW1TQ1IkRZA-sCEegNrE",
         "plus_code" : {
            "compound_code" : "QQG5+C3 Toronto, Ontario, Canada",
            "global_code" : "87M2QQG5+C3"
         },
         "rating" : 5,
         "reference" : "CmRSAAAARH4do7wLR_ApVDY6rSkXV7nUqTthQgNrzy8MY74W-RDm8pxUmM97kc7e0Ini1Mawpv_6gd0abZnjvqomGT33XFlcabtro5FKodn_WRfZVlkPkUPnYVNnyMlAnk4xpSSlEhCurL6vBf57bRv9nSkzqSkNGhR3nFRQEA1LxnlBQgmyMcblTWx-Kw",
         "scope" : "GOOGLE",
         "types" : [ "university", "school", "point_of_interest", "establishment" ],
         "vicinity" : "1100 Bellamy Road North, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7828583,
               "lng" : -79.23315339999999
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7840405802915,
                  "lng" : -79.23156051970848
               },
               "southwest" : {
                  "lat" : 43.7813426197085,
                  "lng" : -79.23425848029149
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "bfad0ec11a0c8bd1cd995f55323d96fe618c681a",
         "name" : "Stanford International College",
         "opening_hours" : {
            "open_now" : false
         },
         "photos" : [
            {
               "height" : 527,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/110690499199683069802/photos\"\u003eStanford International College\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAgO2qK2GP5lRj3wTk4ZeEuIKP7d1tca5g4mJ5u5lzsdJwXKou6Sr2t1RRrm8ajEbIa4ugJMOlQrDXHyNG8lKG42xUMvrvAMgmxtG9Y4esTadQLMyWr5rn6jeU_b9PeeldEhDKARavSFrwmY1uuXbOKmFfGhQnFHqTGpObKYVYbZKTH40FmkzvKg",
               "width" : 900
            }
         ],
         "place_id" : "ChIJLwa5P1nS1IkR_TW9m-DAyq4",
         "plus_code" : {
            "compound_code" : "QQM8+4P Toronto, Ontario, Canada",
            "global_code" : "87M2QQM8+4P"
         },
         "rating" : 4.3,
         "reference" : "CmRSAAAAEabblJZilp8NctD9LSgX1i9jTi6AHZ8B5qnyonNxYbs4HkrAK0ioHtFb2MwCE9361OpaZJYMk6FEXbOGhrgiSDAVoZhu0QLW-vKBDJbqnJ1cjOTLMpUr8MozuTOVzqUCEhCMqKNYQ2oNfks1oE4dtAqiGhSHEmgB7cyXEumxkLKgdk6TJCOmcg",
         "scope" : "GOOGLE",
         "types" : [ "university", "school", "point_of_interest", "establishment" ],
         "vicinity" : "930 Progress Avenue, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7786471,
               "lng" : -79.2475309
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7797950802915,
                  "lng" : -79.24580021970849
               },
               "southwest" : {
                  "lat" : 43.77709711970851,
                  "lng" : -79.24849818029149
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "f6f1149261cbbac12f4120ee95a1a272b973fbb7",
         "name" : "Al Haadi School",
         "opening_hours" : {
            "open_now" : false
         },
         "photos" : [
            {
               "height" : 720,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/109212665903256363685/photos\"\u003eHussein Mallah\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAgSj5M1oiDVbfSNLsr6emtED4tHgK5DnLWs5U6P3itvFKbhoOZVZitOEfdSA02MdnmXNY4-GTiWqrqYv5eyxl30G4VrtGa9rGb_rhbKlrWkRBgGg8kXzJcUiQokDc7jXQEhC9V3yvJ-ibNd5MXL4CAmVMGhT507ComREDEAKuptKZnuIHSuVdnQ",
               "width" : 1280
            }
         ],
         "place_id" : "ChIJy3-ke6rR1IkRo7Q4wlzSPWk",
         "plus_code" : {
            "compound_code" : "QQH2+FX Toronto, Ontario, Canada",
            "global_code" : "87M2QQH2+FX"
         },
         "rating" : 3.4,
         "reference" : "CmRRAAAAkvse29RI_uSWjD7I5mCteZtI1lf9W4IINHE4yv3NBupOvpH36RERJBNI0QIDU3c6ODD34_OPMRGG25SNJ8z_VSGt-whJkUexl3khk4TcgjupCZ13V31vHXOMo03hi1O5EhBa5WJKlTxElbUN2wByOHD-GhSF0Rfrmrl14TDhGB-fjtWiV8e2GA",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "690 Progress Avenue, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.77625529999999,
               "lng" : -79.2359637
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.77730103029149,
                  "lng" : -79.23449316970849
               },
               "southwest" : {
                  "lat" : 43.77460306970849,
                  "lng" : -79.23719113029149
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "d1b239331956e04765a412754008bc8a60451d39",
         "name" : "Toronto Collegiate Institute",
         "opening_hours" : {
            "open_now" : true
         },
         "photos" : [
            {
               "height" : 511,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/104861771256862054562/photos\"\u003eToronto Collegiate Institute\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAsXFyg2ol3VFSDKR_8cbHsF6NEYCd6EgdFPeDA_3r1oynazT6BBS3SKsc2K9qtS5uPyO1KqNkWyQhgsEOT0hYk3urKO180WgArT4i86gE-VaBYQye1Ctp8S-S79EhvXCgEhB9NKk0TRaW9b_D3z27MgK3GhQl0WDaBb9ZPxyaRFiOTk-UV3OkIg",
               "width" : 682
            }
         ],
         "place_id" : "ChIJzbQ0xHfR1IkRmUphAE8gNpI",
         "plus_code" : {
            "compound_code" : "QQG7+GJ Toronto, Ontario, Canada",
            "global_code" : "87M2QQG7+GJ"
         },
         "rating" : 3.3,
         "reference" : "CmRSAAAAqJw8WTEa46XVqrtmcbj1cbgYfAQN82TyohIk5GGhmc9iU4mYAx8FCaNy3VV8vAZhLzROZTrCvhw0F41rDm9MmLUqV_GlGYMPYL2llpTSQKNAYiFAwG-z_-tamZ88h6uIEhDZ4oOJMVHr9ZHDjpVMyZwLGhTVapy2kdSTYkkS_PHAcnTohKGeqw",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "2020 Ellesmere Road #2, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.787995,
               "lng" : -79.2475785
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7893646302915,
                  "lng" : -79.24624506970849
               },
               "southwest" : {
                  "lat" : 43.7866666697085,
                  "lng" : -79.2489430302915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "0801b2153b7292d67f55dde66640a50ac6d39ea8",
         "name" : "White Haven Public School",
         "photos" : [
            {
               "height" : 1184,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/117857877915654816511/photos\"\u003eBrad Corner\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAMY64_fFfIKKiwjTLZXD6vP4RblF-JnU-8USvmTMyqbadcKJzSUCdeEcqKp_5Bqt2Tkn-LN95leHAx0dzMSv-ODoYyM7enNZLCf0A9LyI0sAOcxjd4OdnLdUczfTpKAs1EhCbOV2n7MLaSHGfl_qwL6eqGhTIeg99yzm4emuXdhjR1vp41z-8vw",
               "width" : 1776
            }
         ],
         "place_id" : "ChIJh_ih1ALR1IkRCz6RaVNe2yE",
         "plus_code" : {
            "compound_code" : "QQQ2+5X Toronto, Ontario, Canada",
            "global_code" : "87M2QQQ2+5X"
         },
         "rating" : 3.4,
         "reference" : "CmRRAAAAagmZyXosmAWS8aIcWPCnDGouXNmG3o50qxqe4-oNBil2KRI4vWq5SWxwb0RcjF0g2ZMjkjJz5sM6-CmyXECAcugNResxQYTSx4p3sllHF_oylPxhc4ViONaPoGlhm4VeEhDz4paPeNgmukd5ArYggi4JGhQNrIdHl_l1m9KgWZ14C_wH9oKYuA",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "105 Invergordon Avenue, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7780168,
               "lng" : -79.2477004
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7792365802915,
                  "lng" : -79.24617376970849
               },
               "southwest" : {
                  "lat" : 43.7765386197085,
                  "lng" : -79.24887173029151
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "856f8f0cfe2ba09ef5e9553eb085434f0f24a1f3",
         "name" : "Madinatu Uloom Islamic School",
         "photos" : [
            {
               "height" : 2160,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/117014941308701602689/photos\"\u003efmhnj gbjkkok\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAwM1QXynl4h2cl-GTGUoG2CyQdGc7OLRsYTQfCK1LT8q2M8i6gcE6JUXUMJJMGt1fvEQzLTGiOywTvhEYQ0HFjffSZpdvF1MtgGOXLBxRQCufxvTHyqBk0kVioT6mTDVlEhBq4BPF4ol22G_aWSQz8I76GhTKS3JUpJntylfBvkfVoEc5untL9w",
               "width" : 3600
            }
         ],
         "place_id" : "ChIJNSboL6rR1IkR2dJwZqN6IXI",
         "plus_code" : {
            "compound_code" : "QQH2+6W Toronto, Ontario, Canada",
            "global_code" : "87M2QQH2+6W"
         },
         "rating" : 3.3,
         "reference" : "CmRRAAAAuc1-mN83Is9RnKR4HuWc6CDGw0UF8eBhvdvJCaucz0Ke92mu7eV8g_W4PKFbDSZr8UTIuxdF_yb0EaURSgVzGJY3ky2kI7yyOE4SmMEdhrx22-qKo2mHlN_kXBfQ0OGjEhDvkLr1UoqTnPC7bRZ1t__jGhQNovOtOyKoT4DQKuVhGriucGsxFQ",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "700 Progress Avenue, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.78883789999999,
               "lng" : -79.2467889
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.79018688029149,
                  "lng" : -79.24543991970849
               },
               "southwest" : {
                  "lat" : 43.78748891970849,
                  "lng" : -79.24813788029151
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "64e2bf5f5d6385a5c11e09693d8ad28da6a6a410",
         "name" : "St. Elizabeth Seton Catholic School",
         "photos" : [
            {
               "height" : 1184,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/117857877915654816511/photos\"\u003eBrad Corner\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAtJdEB5-JcEWrwuXUZYSmnBLZ6JnBgkqyVUe1MVYiILp65GREHfJfE9UOspzi-P_dkhdXCU5nBVKYQ2U5n01EZ35B2iB4uyYREIu8Wchm4Erep5Pqh2boVpNbtbH8jA9jEhDCP7Ge1is_nkQoFnUsShzeGhSA13BlU7DC1KLeTufuaFr33oZOTA",
               "width" : 1776
            }
         ],
         "place_id" : "ChIJ80s0Mh3R1IkRADVxM3AbNtQ",
         "plus_code" : {
            "compound_code" : "QQQ3+G7 Toronto, Ontario, Canada",
            "global_code" : "87M2QQQ3+G7"
         },
         "rating" : 3.8,
         "reference" : "CmRSAAAAHIw-3YmvoVUwbB_PwrhZ7zKS3BJRUHOUFsDJBXKvwxgA_iRFJF55a0hzdxvb_DBhAEl2xHxMUErYChMQSmrQaRqvG4Qaz7VauLHj0jKWixVlzErgCwKwQHwmjBrCIbuTEhATPuQ8Rnh62UFhorYkr8z3GhSX1AQXbx5FNYFqP0UwnGC546JgVw",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "Toronto"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7927577,
               "lng" : -79.2289182
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7941714302915,
                  "lng" : -79.22787186970849
               },
               "southwest" : {
                  "lat" : 43.7914734697085,
                  "lng" : -79.2305698302915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "0399cdcde9097b2ef0cca82c2b4fac4c727c03e5",
         "name" : "Burrows Hall Junior Public School",
         "place_id" : "ChIJI5CHcunQ1IkRKHmCITicY3g",
         "plus_code" : {
            "compound_code" : "QQVC+4C Toronto, Ontario, Canada",
            "global_code" : "87M2QQVC+4C"
         },
         "rating" : 4.5,
         "reference" : "CmRRAAAAAYmv0SSi-p9ydLuQzwMac8X3EuK9nw7hIBUD3GxHkJDfCkSwDx5kAVkELhbkxNe5vpscResyuB4H481EzYZBUjYaVWyR1BogKLDTia8yyb-ivYGjC96SqkLsQxBZSHJxEhB1na6Bm38BOQ5YwHzm-2brGhQT0EGmr8CVBqrp2AU-yKqenq1MaQ",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "151 Burrows Hall Boulevard, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7829592,
               "lng" : -79.23337719999999
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7840772802915,
                  "lng" : -79.23169041970849
               },
               "southwest" : {
                  "lat" : 43.7813793197085,
                  "lng" : -79.23438838029149
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "30f4581841c2e6b2c3fa6ef4476fd7600982da1b",
         "name" : "Armenian General Benevolent Union Day School",
         "place_id" : "ChIJGRJ0rPDQ1IkREuXgC30Kk6Y",
         "plus_code" : {
            "compound_code" : "QQM8+5J Toronto, Ontario, Canada",
            "global_code" : "87M2QQM8+5J"
         },
         "reference" : "CmRSAAAAOjz1YBCOudfu7ArwNHmb6H0hrQ9mKudHPs3V2Lgr_Y5UQNYXKzKG_OwnlWPifc9roKCiD8P45mU6Bq6v3AU3CGByfKUz-_D1I5vpirKoW1Us4b9ngJs_wvhj55ez4m_pEhCQ7kqkKDl0ACAHOJ4ht4-mGhTOMw21Eth-jrnJKdeqES7Dx9mbCw",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "930 Progress Avenue, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7783833,
               "lng" : -79.22517069999999
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7795127302915,
                  "lng" : -79.22372966970848
               },
               "southwest" : {
                  "lat" : 43.7768147697085,
                  "lng" : -79.2264276302915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "e3960bc1ac2c6f74997582b223f524edde277d03",
         "name" : "St. Thomas More Catholic School",
         "photos" : [
            {
               "height" : 2610,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/105063174720844856268/photos\"\u003eAntony Isac\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAr5iPCZF5ots1EoOchZXKJdNuDHfsZ97ZRa_7cohQ2KedbOmlCd1rcvLmGmftEqFtbFtIxvMVqsg-eHK4VwVXYT6jOJhzLWCmTuXRySRFObCjrtQrWYnGs85OezorF8LoEhBgC2I5q3hppJU2BfY_xMYwGhT7XKeJu_J_HnTx9N57IG-30uTl3A",
               "width" : 4640
            }
         ],
         "place_id" : "ChIJfVz1JPXQ1IkRJ5EIIIdOuXo",
         "plus_code" : {
            "compound_code" : "QQHF+9W Toronto, Ontario, Canada",
            "global_code" : "87M2QQHF+9W"
         },
         "rating" : 3.6,
         "reference" : "CmRRAAAAy8IKowxJk4n1QIXXsTllJuKOZ7xtJmi4llEwb0BWiIuMiVA0RnBUNUZjIsyu3VajD4dwpP4UXmYhII_Rjd79HV7q9mGwytse1j48zO1-KAKCl2AiSUkk4uz8noAVjaU_EhBjnzPRoBzvB1f_G0H4CxJYGhTOuPmvXN9de0go3_E2p_XtZSnVZw",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "2300 Ellesmere Road, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7730799,
               "lng" : -79.22391560000001
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.77461458029151,
                  "lng" : -79.2223856697085
               },
               "southwest" : {
                  "lat" : 43.77191661970851,
                  "lng" : -79.22508363029151
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "ceadf41eef48684335807ac93349d20f128059f4",
         "name" : "Churchill Heights Public School",
         "photos" : [
            {
               "height" : 1080,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/110235660169592962551/photos\"\u003eVineel Sharma\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAbfszCubIrUSSaxtNbMM4j3nQEJwYTwLX5CzUMmQp-n7eQhjxwryvyc4ZVvsOFAo6REVhxUdPEt9yiBGdCgASywtqvxwa026Y_H1kmeN3u1oCCDW9W3fpyZVYrtJnBOXzEhDa7Qjg5M5clENC0XFWGAbgGhQlR5DGQxvd6djCFa7cHHMrReW1HQ",
               "width" : 1920
            }
         ],
         "place_id" : "ChIJkTccSl7Q1IkRgCRq_C4HsBg",
         "plus_code" : {
            "compound_code" : "QQFG+6C Toronto, Ontario, Canada",
            "global_code" : "87M2QQFG+6C"
         },
         "rating" : 3.2,
         "reference" : "CmRRAAAA6za4qnl5Hp7UShDOjbTTQrk_rL3usEnWw1t7KLZS920PGMMbDRrqevCQObvDpurSWL1FGIe9XUhDdjikgabRub02qL5vPIsQ_9ks4023Dfb0IwNdSqFcsVawCNusOVriEhBC_lFEiN_tgAZEjXE5z0vKGhS_KnLAN6tpEyXRmByk2TD4LvmSBw",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "749 Brimorton Drive, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7792041,
               "lng" : -79.226443
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7805339302915,
                  "lng" : -79.2250065697085
               },
               "southwest" : {
                  "lat" : 43.77783596970851,
                  "lng" : -79.2277045302915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "7f090fed9cc2d866356cbb01b54f660cf476622e",
         "name" : "Woburn Junior Public School",
         "place_id" : "ChIJW8lFs_XQ1IkREH4okJ-ohj4",
         "plus_code" : {
            "compound_code" : "QQHF+MC Toronto, Ontario, Canada",
            "global_code" : "87M2QQHF+MC"
         },
         "rating" : 4.7,
         "reference" : "CmRRAAAAYR0FXwokXiSY-X8Cf3XhPfpAGizze66eXHCUfvcNKYDzmCJ68zCsWBgl7zK8GXRUpUdDUVI0I3iyCpteBr2YYOWVjDrr6Q5tLLcTQVFFvNjU2Si7OTzCgRKGqMOZ4HeOEhBiANZvuVEdbSrfyQPQFPuYGhRu2vf_dto7QDRT4HcE9HzEWphcow",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "40 Dormington Drive, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.768001,
               "lng" : -79.2291123
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.76940368029149,
                  "lng" : -79.2275048697085
               },
               "southwest" : {
                  "lat" : 43.7667057197085,
                  "lng" : -79.2302028302915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "d0e2b4694deb7f8f005c9ba1834e58e0b4e5c741",
         "name" : "Crown Driving Academy Inc.",
         "opening_hours" : {
            "open_now" : true
         },
         "place_id" : "ChIJiy7BW1vQ1IkRI41SZRjDvkk",
         "plus_code" : {
            "compound_code" : "QQ9C+69 Toronto, Ontario, Canada",
            "global_code" : "87M2QQ9C+69"
         },
         "rating" : 4.9,
         "reference" : "CmRRAAAAPqu42fKkbuA9CKcu42ldKzcAxuR9CXW1XPkB9EU24uQri_mj2CIN7IjbsSNkB-y-4KwKJ31XpDlHtbNzru5JYugPBygYhu4CHmfdjZOBZMT3htg_sZCVe80BNSWnS8hxEhBudr5k6VeUl3bxRVs5g6c3GhQmeaDSzALWTjtnz3iaSt_3kdCAVA",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "868 Markham Road #109, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.77183970000001,
               "lng" : -79.23512749999999
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7729916802915,
                  "lng" : -79.23354226970848
               },
               "southwest" : {
                  "lat" : 43.7702937197085,
                  "lng" : -79.2362402302915
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "fe5189801e979a9eb28160021cca725c7150d47f",
         "name" : "Bellmere Junior Public School",
         "photos" : [
            {
               "height" : 3036,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/106730437608231316574/photos\"\u003eBhavesh Patel\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAqa1PxeeqyGvbjJNZIMtFFQuEr8Ya1PFGP9hvp4STxweQ7XSUJ6KDxJ3--ULZ1F3gbiXsTzTLfQEm1_NdMT16gGMEHfsoA5lIM0iIemfq6xsZ525bG4De7ep3xQ9KBGrqEhDyEcLpVWRG55OFX-HiXbiDGhR_Fr5kSeFPDircx8_VHH_qdMFCCg",
               "width" : 4048
            }
         ],
         "place_id" : "ChIJxarzuFDQ1IkRgw2Phyrs1Ps",
         "plus_code" : {
            "compound_code" : "QQC7+PW Toronto, Ontario, Canada",
            "global_code" : "87M2QQC7+PW"
         },
         "rating" : 3.8,
         "reference" : "CmRSAAAAFL8fTrN5f4xYrhzxfmiHLBw60BbZaVHmLHJeaEJNm-NUNCLH6wOeu3WxyDgLUg2SAWMzOd2w0pxEhhtccQkGHfck8Rjt1Tihc4Pkbbnb7Vrek_pVxU-27RcsTTfqEVBEEhBt4VW-S6c6z64qAgfkitVCGhTo4YhgBi7Y5YKgDDsbQEOk5m3KKw",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "470 Brimorton Drive, Scarborough"
      },
      {
         "geometry" : {
            "location" : {
               "lat" : 43.7898495,
               "lng" : -79.23330829999999
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 43.7914217802915,
                  "lng" : -79.23205311970848
               },
               "southwest" : {
                  "lat" : 43.7887238197085,
                  "lng" : -79.23475108029149
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/school-71.png",
         "id" : "71f90f2c457c32ad35d4b3a694a534405a6d1f4f",
         "name" : "Aisling Discoveries Child & Family Centre",
         "place_id" : "ChIJESlbXe_Q1IkRbKXPPZoFYSA",
         "plus_code" : {
            "compound_code" : "QQQ8+WM Toronto, Ontario, Canada",
            "global_code" : "87M2QQQ8+WM"
         },
         "rating" : 4,
         "reference" : "CmRRAAAAD0Pc8X1dhQsyCOtNvA1FCysYkjzjNIaEFs9Tfy5av9R2gBUiAto8SFSl1ftbgHcJZHcDdVU6AZa6840UcUAs6i-v9WEk551HS9Rz0Uy92qnPnjkNornLmxDdkgVO_rwPEhBbsZ_PfUjDUfdWEQxiJi9sGhT5rd-JOUhIfVwwI5_pH0da7YfaJQ",
         "scope" : "GOOGLE",
         "types" : [ "school", "point_of_interest", "establishment" ],
         "vicinity" : "325 Milner Avenue, Scarborough"
      }
   ],
   "status" : "OK"
}
    */
    // 'ctrl + O' to generate override fns
    @Override
    protected void onPostExecute(String s)//JSON string passed
    {
        //read hard-coded JSON bcoz API is severely limited by Google
        try
        {
            //https://stackoverflow.com/questions/6420293/reading-android-raw-text-file
            InputStream is  = ctx.getResources().openRawResource(R.raw.nearbyclinics);
            byte[] buffer = new byte[is.available()];
            while(is.read(buffer) != -1);
            s = new String(buffer);
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            Log.e("raw file reading:", ""+e.toString());
        }

        //
        List<HashMap<String,String>> nearbyPlacesList = null;
        DataParser parser = new DataParser();
        nearbyPlacesList = parser.parse(s);//parse() gets List<HashMaps<ea Place>> by passing JSON str from Google to => getPlaces() gets HashMap<ea place> by passing ea json-array-str to => getPlace()
        showNearbyPlaces(nearbyPlacesList);


        //super.onPostExecute(s);
    }

    //Add markers to map to show ALL places - move camera to marker
    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlacesList)
    {
        for (int i = 0; i < nearbyPlacesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlacesList.get(i);
            String placeName = googlePlace.get("placeName");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("latitude"));
            double lng = Double.parseDouble(googlePlace.get("longitude"));

            LatLng latLng  = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);

            mMap.addMarker(markerOptions);//add multiple markers, one for ea place

            //move Camera to:
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
