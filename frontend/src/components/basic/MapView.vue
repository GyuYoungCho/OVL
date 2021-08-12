<template>
    <div>
        <div id="map" style="width: 100%; height: 200px; margin: auto;"></div>
    </div>
</template>

<script src="http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=09778f1fe1acff44feef56aa1e949d9b&libraries=services"></script>
<script>
import { mapGetters, mapActions } from 'vuex';
export default {
    data(){
        return{
            markers : [],
            map : null,
            selectedMarker : null,
            selectedInfo : null,
            poti : null,
        }
    },
    props:{
        step : String
    },
    computed: {
        ...mapGetters('pot',['potitems']),
    },
    mounted() { 
        if (window.kakao && window.kakao.maps) {
            this.initMap();
        } else {
            const script = document.createElement('script');
            /* global kakao */
            script.onload = () => kakao.maps.load(this.initMap);
            script.src =
            'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=09778f1fe1acff44feef56aa1e949d9b&libraries=services,clusterer';
            document.head.appendChild(script);
        }

        
    }, 
    
    methods : { 
        
        initMap() {
            var container = document.getElementById('map');
            var options = { 
                center: new kakao.maps.LatLng(37.542161, 127.068755),
                level: 8,
            }; 
            
            this.map = new kakao.maps.Map(container, options); 
            this.addMarkers(this.potitems)
        }, 
        
        
        addMarkers(datas){
            this.deleteMarkers()
            
            if(!datas) return;
            var imageSrc = require('@/assets/image/marker.png'),
            imageSize = new kakao.maps.Size(30, 40), // 마커이미지의 크기입니다
            imageOption = { offset: new kakao.maps.Point(30, 40) }; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
            var geocoder = new kakao.maps.services.Geocoder();
            var map = this.map
            // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
            var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
            var coords = null
            datas.forEach(pos=> {
                let addr = pos.place.split(' ')
                let address = addr[0]+" " +addr[1]+" " +addr[2] +" " +addr[3]
               
                geocoder.addressSearch(address, function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {

                        coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                    } 

                    var marker = new kakao.maps.Marker({
                        clickable:true,
                        position: coords,
                        image: markerImage,

                    });
                    
                    marker.setMap(map);
                    
                    // this.markers.push(marker);

                var iwContent =`<center>[${pos.time}] <br> ${pos.restaurantName}<br><a href="https://map.kakao.com/link/to/Hello World!,33.450701,126.570667" style="color:blue" target="_blank">길찾기</a></center>`;
                 

                var infowindow = new kakao.maps.InfoWindow({
                    content : iwContent,
                    removable : true,
                });

                kakao.maps.event.addListener(marker, 'mouseover', ()=> {
                    infowindow.open(this.map, marker);
                });

                // 마커에 마우스아웃 이벤트를 등록합니다
                kakao.maps.event.addListener(marker, 'mouseout',() => {
                    infowindow.close();
                });
                
                
                map.setCenter(marker.getPosition());
                });
                
                
            })
        },

        movemap(pos){
            this.map.setCenter(pos);
        },

        deleteMarkers() {
          this.clearMarkers();
          this.markers = [];
          
        },

        clearMarkers() {
            this.setMapOnAll(null);
        },

        setMapOnAll(map) {
            for (let i = 0; i < this.markers.length; i++) {
                this.markers[i].setMap(map);
            }
        },
    },
}   

</script>