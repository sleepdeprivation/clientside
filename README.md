# clientside

Hello all:

This repo is now public! Hooray!

I have purged the readme file and the commits relating to it are gone. Please be careful that you don't push it up.

If you're curious, the instructions I used are here:

https://help.github.com/articles/remove-sensitive-data/

I have distributed to you your new API keys, please put them in your manifests.

But **beware**! before you go and push that manifest up and expose the new key, do:

````bash
git update-index --assume-unchanged app/src/main/AndroidManifest.xml
````

This will cause your manifest to no longer get pushed.

If the manifest needs to be changed, we'll have to work around this, though I'm not sure I forsee that in the near future.

-Christian Burke
