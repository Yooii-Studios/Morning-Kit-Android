사용법:

1. Eclipse:
    SKAlarmManager.java 를 복사해서 사용하세요.

2. Android Studio:
    1) SKAlarmManagerSample 프로젝트를 자신의 프로젝트와 같은 루트 폴더로 복사합니다.
    2) 루트 프로젝트의 settings.gradle에서 다음의 코드를 추가합니다.

        include ':libraries:SKAlarmManager'
        project (':SKAlarmManager').projectDir = new File(settingsDir, '../SKAlarmManagerSample/SKAlarmManager')

    3) 사용하려고 하는 본 프로젝트의 build.gradle의 dependencies에서 다음의 코드를 추가합니다.
    
        compile project(':libraries:SKAlarmManager')

    4) Sync Project with Gradle Files 를 클릭하면 해당 프로젝트를 사용할 수 있습니다.

----------------------------------------------------------------------------------------------------

Release Notes

0.1.0 버전:
- 최초로 샘플 프로젝트와 SKAlarmManager를 통합
- 루트 프로젝트의 setting.gradle의 link를 통해 복사하지 않고 사용 가능하게 고안


