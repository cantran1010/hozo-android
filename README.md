# hozo-android [![Build Status](https://travis-ci.com/wearetonish/hozo-android.svg?token=CnyzdKBZqSx1AyCVmYCE&branch=master)](https://travis-ci.com/wearetonish/hozo-android) [![Build status](https://badge.buildkite.com/a4d2e23c9fb91d3de4d15abf465eaf9d962c4a58ab0e448905.svg)](https://buildkite.com/tonish/hozo-android) [![Build Status](https://www.bitrise.io/app/9f7b24d20505cc19/status.svg?token=KQyAQ7B45A-oac8bsCfJJg&branch=master)](https://www.bitrise.io/app/9f7b24d20505cc19)

Hozo for Android

## Deploy
1. Copy json key file under the root directory of this project.
1. Store the following info to your `$HOME/gradle/gradle.properties`:

        RELEASE_STORE_FILE={path to your keystore}
        RELEASE_STORE_PASSWORD=*****
        RELEASE_KEY_ALIAS=*****
        RELEASE_KEY_PASSWORD=*****

1. To deploy a new version to Google Play:

        bundle exec fastlane deploy
