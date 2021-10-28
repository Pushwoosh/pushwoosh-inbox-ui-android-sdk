# Pushwoosh Inbox UI

## Installation
To add Inbox UI to your Android project, you should add Kotlin support and Pushwoosh Inbox libraries. Add the following lines to your project's build.gradle:

```
buildscript {
    ext.kotlin_version = '1.5.10'

    dependencies {
        ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}
```

and add the following lines to your app's build.gradle:

```
...
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
....

ext.pushwoosh = "6.+"

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.32"

    implementation "com.pushwoosh:pushwoosh-inbox:$pushwoosh"
    implementation "com.pushwoosh:pushwoosh-inbox-ui:$pushwoosh"
    ...
}
```

## Implementation
To show Inbox UI in your app you can:
* Start an activity by using following line:
```
startActivity(new Intent(context, InboxActivity.class))
```
* Attach a fragment to container:
```
getSupportFragmentManager().beginTransaction()
                    .replace(R.id.inboxContainer, PushwooshInboxUi.INSTANCE.createInboxFragment())
                    .commitAllowingStateLoss()
```

## Proguard rules
If you enable code shrinking with proguard, add the following rule to your proguard rules:
```
-keep public class com.pushwoosh.inbox.PushwooshInboxPlugin {
 *;
}
```

## Customization
You can customize Inbox UI style. Check [attrs.xml](InboxUiLibrary/pushwoosh-inbox-ui/src/main/res/values/attrs.xml) for all attributes that you can change. Also you can change Inbox UI style from code. To do so, please see [PushwooshInboxStyle](InboxUiLibrary/pushwoosh-inbox-ui/src/main/java/com/pushwoosh/inbox/ui/PushwooshInboxStyle.kt). For more details, take a look at [Inbox sample](InboxSample).
