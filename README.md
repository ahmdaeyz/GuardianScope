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

