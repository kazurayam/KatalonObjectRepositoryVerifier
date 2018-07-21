Katalon Studio Object Repository Verifier and Transformer
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

This Groovy script employs the XPath technology bundled in the JDK as `javax.xml.xpath.XPath` and other classes.


### `Test Cases/ObjectRepositoryTransformer`

This Groovy script employs the XSLT technology bundled in the JDK as `javax.xml.transform.Trasformer` and other classes.


## How to run the demo

writer: kazurayam
