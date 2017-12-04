# Pushwoosh Inbox UI

## Installation
To add Inbox UI to your Android project, you should add Kotlin support and Pushwoosh Inbox libraries. Add the following lines to your project's build.gradle:

```
buildscript {
    ext.kotlin_version = '1.1.60'

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

ext.googlePlayService = "11.+"
ext.support = "27.+"
ext.pushwoosh = "5.+"

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"

    implementation "com.android.support:appcompat-v7:$support"
    implementation "com.android.support:recyclerview-v7:$support"
    implementation "com.android.support:design:$support"

    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    implementation "com.github.bumptech.glide:glide:4.3.1"

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
## Customization
You can customize Inbox UI style. Check [attrs.xml](InboxUiLibrary/pushwoosh-inbox-ui/src/main/res/values/attrs.xml) for all attributes that you can change. Also you can change Inbox UI style from code. To do so, please see [PushwooshInboxStyle](InboxUiLibrary/pushwoosh-inbox-ui/src/main/java/com/pubshwoosh/inbox/ui/PushwooshInboxStyle.kt). For more details, take a look at [Inbox sample](InboxSample).
