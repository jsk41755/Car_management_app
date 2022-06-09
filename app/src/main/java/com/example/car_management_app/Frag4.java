package com.example.car_management_app;

import static android.content.Context.LOCATION_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Frag4 extends Fragment {

    String BASE_URL = "https://dapi.kakao.com/";  // 카카오 REST API 가져올 사이트 주소
    String KakaoAK = "KakaoAK 81bb49d29320f177f9d28498acbe9a28"; // 카카오 MAP 네이티브 API 앱 키

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    //위치 퍼미션 수락하기 위한 용도
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private FusedLocationProviderClient fusedLocationClient;

    Context ct;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_tab4_fragment,container,false);

        ct = container.getContext();

        //카카오 맵 뷰 출력
        MapView mapView = new MapView(ct);

        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 위치 권한 확인 및 수락
        if (!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();} //위치 권한 확인 다이얼로그 출력
        else{
            checkRunTimePermission(mapView); // 퍼미션 확인
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ct); //현재 위치 받아들이기
        fusedLocationClient.getLastLocation() // 마지막으로 저장된 위치 받아들이기
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) { //마지막 위치 받아들이는거 성공시
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            double currLat = location.getLatitude(); //현재 위치 위도 알아오기
                            double currLon = location.getLongitude(); //현재 위치 경도 알아오기
                            String cur_address = getCurrentAddress(currLat, currLon);

                            String[] address = cur_address.split("\\s");

                            //현재 위치 표시하기
                            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(currLat, currLon), true);
                            mapView.setZoomLevel(4, true);
                            startMarker(mapView, currLat, currLon);

                            MapPoint mapPoint = mapView.getMapCenterPoint();
                            MapPoint.GeoCoordinate curMappoint = mapPoint.getMapPointGeoCoord();
                            Log.d("확인",address[1] + address[2]);
                            
                            // 현재 위치 기반 키워드로 근처 정비소 장소 알아오기 
                            searchKeyword(address[1] + " "+  address[2] + "정비소", mapView);
                        }
                        else{
                            
                            //현재 위치 알아오는것 실패할 시 default 값으로 알아오기
                            searchKeyword("청주 정비소", mapView);
                        }

                    }

                });


        /* 다른 방법으로 현재 위치 알아오기 2
        fusedLocationClient.getCurrentLocation(5, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                Log.d("실패1","위치");
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                Log.d("실패2","위치");
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double currLat = location.getLatitude();
                double currLon = location.getLongitude();
                String cur_address = getCurrentAddress(currLat, currLon);

                String[] address = cur_address.split("\\s");

                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(currLat, currLon), true);
                mapView.setZoomLevel(4, true);
                startMarker(mapView, currLat, currLon);

                MapPoint mapPoint = mapView.getMapCenterPoint();
                MapPoint.GeoCoordinate curMappoint = mapPoint.getMapPointGeoCoord();
                Log.d("확인",address[1] + address[2]);
                searchKeyword(address[1] + " "+  address[2] + "정비소", mapView);
            }
        });
         *//*

        */
        /* 다른 방법으로 현재 위치 알아오기 3
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//마지막 위치 받아오기
        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc_Current == null)
        {
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            loc_Current = bestLocation;
        }
        double cur_lat = loc_Current.getLatitude(); //위도
        double cur_lon = loc_Current.getLongitude(); //경도


        String cur_address = getCurrentAddress(cur_lat, cur_lon);


        String[] address = cur_address.split("\\s");

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(cur_lat,  cur_lon), true);
        mapView.setZoomLevel(4, true);
        startMarker(mapView, cur_lat, cur_lon);


        MapPoint mapPoint = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate curMappoint = mapPoint.getMapPointGeoCoord();
        Log.d("확인",address[1] + address[2]);
        searchKeyword(address[1] + " "+  address[2] + "정비소", mapView);

         *//**/
        return v;
    }
    
    //퍼미션 확인
    public void checkRunTimePermission(MapView mapView) {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(ct,Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),REQUIRED_PERMISSIONS[0])){
                Toast.makeText(ct,"이 앱을 실행하려면 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //위치 권한 확인하는 다이얼로그 출력
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ct);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent,GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.create().show();
    }

    //현재 서비스 상태 체크하기
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) ct.getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //키워드로 주변 장소 이름 검색 및 경도 위도 알아오기
    private void searchKeyword(String keyword, MapView mapView){
        //키워드로 찾기
        Call<GetResultClass> call = RetrofitNet.getRetrofit().getService().getDocuments(KakaoAK, keyword);

        call.enqueue(new Callback<GetResultClass>() {
            @Override
            public void onResponse(Call<GetResultClass> call, Response<GetResultClass> response) {
                if(response.isSuccessful()){
                    GetResultClass result = response.body();
                    Log.d("성공", result.toString());
                    MapPOIItem marker = new MapPOIItem();
                    for (int i = 0; i < result.documents.size(); i++)
                    {
                        Log.d("성공", result.documents.get(i).getAddress_name());
                        Log.d("성공", result.documents.get(i).getPlace_name());
                        Log.d("성공", result.documents.get(i).getY());
                        Log.d("성공", result.documents.get(i).getX());
                        setMarker(marker, mapView,
                                result.documents.get(i).getY(),
                                result.documents.get(i).getX(),
                                result.documents.get(i).getPlace_name());
                    }
                } else {
                    Log.d("실패", "실패");
                    Log.d("실패", call.toString());
                    Log.d("실패", response.toString());
                    Log.d("실패", KakaoAK);
                }

            }

            @Override
            public void onFailure(Call<GetResultClass> call, Throwable t) {
                Log.d("실패", "예외");
                Log.d("실패", call.toString());
                Log.d("실패", t.toString());
            }
        });
    }
    //카카오맵에 마커 표시하기
    public void setMarker(MapPOIItem marker, MapView mapView, String x, String y, String name){
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(x), Double.parseDouble(y));
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(mapPoint);

        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(marker);
    }
    //현재 내 위치 마커로 표시하기
    public void startMarker(MapView mapView, double x, double y)
    {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x,y);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 내 위치");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);


        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        marker.setCustomImageResourceId(R.drawable.carimg);

        mapView.addPOIItem(marker);
    }
    // 현재 주소 알아오기
    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(ct, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(ct, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(ct, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(ct, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }
}
