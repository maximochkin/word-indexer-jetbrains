docker run --name eureka -d -p 8010:8010 maximochkin/eureka-server
docker run -d -p 3306:3306 --name db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=index -e MYSQL_USER=storage -e MYSQL_PASSWORD=password mysql:latest
docker run -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -p 8011:8011 -d --name zuul --link eureka:eureka maximochkin/api-gateway
docker run -d --restart=on-failure:10 --name uploader1 -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -e "server.port=8089" -e "eureka.instance.prefer-ip-address=true" -p 8089:8089 --link eureka:eureka maximochkin/uploader
#docker run -d --restart=on-failure:10 --name uploader2 -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -e "server.port=8093" -e "eureka.instance.prefer-ip-address=true" -p 8093:8093 --link eureka:eureka maximochkin/uploader
docker run -d --restart=on-failure:10 --name searcher1 -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -e "server.port=8090" -e "eureka.instance.prefer-ip-address=true" -p 8090:8090 --link eureka:eureka maximochkin/searcher
#docker run -d --restart=on-failure:10 --name searcher2 -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -e "server.port=8094" -e "eureka.instance.prefer-ip-address=true" -p 8094:8094 --link eureka:eureka maximochkin/searcher
docker run -d --restart=on-failure:10 --name cache -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -e "server.port=8091" -e "eureka.instance.prefer-ip-address=true" -p 8091:8091 --link eureka:eureka maximochkin/cache
docker run -d --restart=on-failure:10 --name storage1 -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -e "server.port=8092" -e "eureka.instance.prefer-ip-address=true" -e "spring.datasource.url=jdbc:mysql://db:3306/index" -e "spring.jpa.hibernate.ddl-auto=update" -e "spring.datasource.username=storage" -e "spring.datasource.password=password" -p 8092:8092 --link eureka:eureka --link db:db maximochkin/storage
#docker run -d --restart=on-failure:10 --name storage2 -e "eureka.client.service-url.defaultZone=http://eureka:8010/eureka" -e "server.port=8095" -e "eureka.instance.prefer-ip-address=true" -e "spring.datasource.url=jdbc:mysql://db:3306/index" -e "spring.jpa.hibernate.ddl-auto=update" -e "spring.datasource.username=storage" -e "spring.datasource.password=password" -p 8095:8095 --link eureka:eureka --link db:db maximochkin/storage
echo "wait a minute"
sleep 60
echo "done"
echo "Try using GET localhost:8011/searcher/getFilesByWord?word=blahblah"
echo "Try using POST localhost:8011/uploader/upload"