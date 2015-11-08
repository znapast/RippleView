#RippleView

Examples
-------

Here are some examples of how these rippleview could look like:

![example](http://a.hiphotos.baidu.com/image/w%3D310/sign=63c6e3cc8101a18bf0eb144eae2e0761/472309f7905298229a1c5c23d1ca7bcb0a46d427.jpg)
![example](http://c.hiphotos.baidu.com/image/w%3D310/sign=8f56a91d7d899e51788e3c1572a6d990/8718367adab44aed94dfc404b51c8701a08bfbb9.jpg)
![example](http://a.hiphotos.baidu.com/image/w%3D310/sign=7d0f009a26a446237ecaa363a8237246/5243fbf2b2119313a053855063380cd791238d39.jpg)

#Usage

Gradle
-------

This library now works with gradle and will soon be available on the central maven repository. Just add the following repository to your app build.gradle:

    dependencies {
        // other repos ...
        compile 'com.github.znacloud:rippleview:1.0.0'
    }

Code
-------

After adding the gradle depedency from above you can go to your xml layout and add the following code for a squareprogressbar:
```xml
<com.github.znacloud.RippleView xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/rpv_test"
    android:layout_width="128dp"
    android:layout_height="128dp"
    android:layout_centerInParent="true"
    app:text="TEST"
    app:textColor="@color/text_state_color"
    app:textSize="22sp"
    app:circleColor="@color/circle_state_color"
    app:ringColor="@color/ring_state_color"
    app:bdWidth="4dp"
    app:bdColor="@color/ring_state_color"
    app:shadowRadius="2dp"/>
```

To set some basic settings use the following java-code:
```java
RippleView view = (RippleView)findViewById(R.id.rpv_test);
view.setText("TE+ST");
view.setTextSize(30);
view.setTextColor(getResources().getColorStateList(R.color.text_state_color));
view.setBdColor(getResources().getColorStateList(R.color.ring_state_color));
view.setBdWidth(6);
view.setShadowRadius(16);
view.setRingColor(getResources().getColorStateList(R.color.ring_state_color));
view.setCircleColor(getResources().getColorStateList(R.color.circle_state_color));
```
License
-------

    Copyright 2015-2015 Stephan Zeng
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    

    
    
