# stage1 as builder
FROM maven:3.8.7 as builder

#переменные, которую можно передать при построении команды сборки образа(--build-arg <varname>=<value>)
ARG BRANCH_ARG
#переменные, которые можно юзать при сборке и запуске
ENV BRANCH_ENV=$BRANCH_ARG

#рабочий каталог для всех инструкций RUN, CMD, ENTRYPOINT, COPY и ADD которые будут выполнены в Dockerfile
WORKDIR /tmp
#добавление файла в образ
COPY . .

#сборка jar без тестов
RUN mvn clean package -P $BRANCH_ENV -DskipTests=true

# stage2 as builder
FROM eclipse-temurin:17-jre-alpine

#переменные, которую можно передать при построении команды сборки образа(--build-arg <varname>=<value>)
ARG BRANCH_ARG
#переменные, которые можно юзать при сборке и запуске
ENV BRANCH_ENV=$BRANCH_ARG

#рабочий каталог для всех инструкций RUN, CMD, ENTRYPOINT, COPY и ADD которые будут выполнены в Dockerfile
WORKDIR /opt/app/
#RUN apk add tzdata && cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime && echo "Europe/Moscow" >  /etc/timezone
#Копирование готового jar из stage1
COPY --from=builder /tmp/target/*.jar .

#запуск сервиса
#настраиваем контейнер на работу как испольняеый файл
ENTRYPOINT [ "sh", "-c", "java -jar *.jar --spring.profiles.active=$BRANCH_ENV" ]



