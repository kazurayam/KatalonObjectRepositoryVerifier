Object Repository Verifier / Transformer for Katalon Studio
===============

## What is this?

This is a [Katalon Studio](https://www.katalon.com/) project for demonstration purpose.
You can clone this out onto your PC and execute with your Katalon Studio.

This demo project was developed using Katalon Studio version 5.4.2

This demo project was developed in the hope to help solving problems raised by a post in
the Katalon Forum: "[Is there a way to Mass-Edit many objects in the Object Repository](
 https://forum.katalon.com/discussion/8454/is-there-a-way-to-mass-edit-many-objects-in-the-object-repository)"

The problem was:
1. The questioner has created a mass of Test Objects (>2000) in the 'Object Repository' in a Katalon Studio project.
2. Test Objects can be configured to use various selectors: `id`, `name`, `text`, `xpath`, `class`, `tag`.
2. One day he has made up his mind to standardize the way of selector assignment: use `tag` and `class` only. No longer to use `id` selector.
3. Now he has to reconfigure >2000 Test Objects in the 'Object Repository' manually. It is hard obviously.

## Solution proposed

Manually verifying and reconfiguring massive Test Objects is tiring. Failure tends to occur. Support by software tool is desired.

This project includes 2 test cases.

### `Test Cases/ObjectRepositoryVerifier`

[This Groovy script](https://github.com/kazurayam/KatalonObjectRepositoryVerifier/blob/master/Scripts/ObjectRepositoryVerifier/Script1532075796937.groovy) is designed to meet the following need:

>I want all of my Test Objects to have `tag` selector and `class` selector defined, and these 2 selectors `selected`. How can I check this? I have too many. I want a tool which tells me which Test Objects are missing `tag` and `class` selectors.


[This Groovy script](https://github.com/kazurayam/KatalonObjectRepositoryVerifier/blob/master/Scripts/ObjectRepositoryVerifier/Script1532075796937.groovy) employs the XPath technology bundled in the JDK as `javax.xml.xpath.XPath` and other classes.

The verification criteria is implemented as a set of Strings as XPath expression in the Groovy script. You can customize the verification criteria by modifying the expressions.

### `Test Cases/ObjectRepositoryTransformer`

[This Groovy script](https://github.com/kazurayam/KatalonObjectRepositoryVerifier/blob/master/Scripts/ObjectRepositoryVerifier/Script1532075796937.groovy) is designed to meet the following need:

>I want to rewrite the *rs files in the `Object Repository` directory. The *rs files are in XML format, these are the definition of my Test Objects. I want a tool which transforms mass of *.rs files for me. The resulting Test Objects should have `tag` selector and `class` selector retained, and other types of selectors erased. I want to create a temporary directory in the Katalon project, so that I find all resulting Test Objects are saved there. I do not want the tool change the original 'Object Repository'.

[This Groovy script](https://github.com/kazurayam/KatalonObjectRepositoryVerifier/blob/master/Scripts/ObjectRepositoryVerifier/Script1532075796937.groovy) employs the XSLT technology bundled in the JDK as `javax.xml.transform.Trasformer` and other classes.

The transformation is implemented as a XSLT stylesheet. By modifying the stylesheet, you can customize the way how the *.rs files are transformed.

## How to run the demo

1. clone this project out of GitHub
2. open the project using your Katalon Studio (>5.4)
3. select `Test Cases/ObjectRepositoryVerifier` and run it
4. check the log to see the verification result

and

1. select `Test Cases/ObjectRepositoryTransformer` and run it
2. a new directory `transformed_Object_Repository` will be created in the project home directory.
3. please find the transformed *.rs files
4. If you want to copy the transformed Test Object to the source `Object Repository`, you need to do it manually, one by one.

writer: kazurayam
