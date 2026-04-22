@echo off
title The WECIB Card Game
set PATH=%CD%\javafx-sdk-21\bin;%PATH%
java --module-path "%CD%\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml,javafx.media -jar target\wecib-card-game-1.0-SNAPSHOT.jar
pause