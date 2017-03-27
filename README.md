# Parx
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![](https://jitpack.io/v/JediBurrell/Parx.svg)](https://jitpack.io/#JediBurrell/Parx)

A dynamic XML parser for Android.

**Installation**

In your root build.gradle (If you don't have jitpack already):

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
In your app's build.grade:

	dependencies {
		...
		compile 'com.github.JediBurrell:Parx:-SNAPSHOT'
	}

**Using**

To use, create a new instance of Parx.

	Parx parx = new Parx(context);

Then to parse your XML simply call `Parx.parx()`

	View output = parx.parx(xml);

Then you can use the output anywhere, simply by adding it to your view.

**Refering to views**

Because you're dynamically parsing the XML, you'll need to use dynamic IDs. If you set the id to a static integer, simply call `findViewById(int)`.

Otherwise, you'll want to call `parx.getIds()`. This returns a map with the names of your IDs and the ID integer. For example, if you created a view with the id `@+id/example_id`. You could get it like such:

	findViewById(parx.getIds().get("example_id"));

---

**Contributing**

If you'd like to contribute, simply create a pull request.

The main code is already set up, adding support for any of the unsupported tags and attributes would be extremely helpful.

Please try to stick to the existing coding style.
