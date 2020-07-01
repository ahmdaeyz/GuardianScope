# GuardianScope
A news app with the guardian api as its news source (potentially others in the future!).

## Main features
- Navigate between different categories of articles. 
- Browse trending articles.
- Articles come with their embedded images as if you were reading on the website.
- Bookmark articles for offline reading.
- Search your bookmarks.
- Night Mode!

## Libraries used
- Rxjava2
- RxBinding
- Retrofit
- Material 
- ConstraintLayout 2.0.0 (Used MotionLayout to implement some animations)
- Glide and Glide Transformation
- Threetenabp
- Room 
- Preference
- CircleImageView
- SpinKit
- HtmlTextView
- ExplosionField
- Jetpack Navigation
- LeakCanary

## Do I cache?
Honestly, I thought about using room as a caching store but I felt this is a huge overhead due to two facts:
I retrieve articles without their bodies (because that's a little expensive) and that there is a bookmarking feature
which would be pointless if I used room for caching. So I'm making use of OkHttp http requests caching to minimize
network calls and network usage (though downloading 50 articles without their bodies isn't at all expensive
(but this is better for the cpu?)).

## The architecture and file structure
I think I'm using mvvm. I'm not really sure if I'm adhering to it strictly though. I sometimes use RxJava in fragments (don't worry I clear the composite disposable at onDestroyView :D). I try my hardest to expose only live data objects. Regarding the file structure I'm copying a youtube series I watched by ResoCoder.

## The story behind LeakCanary
Before starting this project, I came across many articles and posts online warning about Memory leaks
and two things stuck with me LeakCanary and Never pass/use context or a reference to a view to a background thread.
I didn't use LeakCanary until I've finished like 90% of the project (If I can say I did :)))
but then I found a pattern that I leaked memory through. I was declaring ViewBinding objects as class fields
although I only used them in OnCreateView/OnCreate. Fixing that across the codebase the app stopped leaking memory
and the garbage collector (and ofc me) were happy.

## Future plans
- I'll start unit and ui testing. 
- Handle Errors more gracefully.
- Improve Loading UX.
- Use sources other than The Guardian.
- Maybe Try Firebase authentication and save users' bookmarks to firestore.
- Improve sharing inapp content.

## Initial app
In this stage, I was using Loaders and AsyncTask to do asynchronous work in addition to having built a Network service using the framework classes (No Retrofit :D) 
</br>
<img src="https://github.com/ahmdaeyz/GuardianScope/blob/master/guardianScope_demo.gif"/>

## Currently 
<img src="https://github.com/ahmdaeyz/GuardianScope/blob/master/guardianScope_latest_demo.gif"/>

