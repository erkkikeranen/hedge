# Hedge · [![Join the chat at https://gitter.im/siilisolutions/hedge](https://badges.gitter.im/siilisolutions/hedge.svg)](https://gitter.im/siilisolutions/hedge?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0) [![Build Status](https://travis-ci.org/siilisolutions/hedge.svg?branch=master)](https://travis-ci.org/siilisolutions/hedge)

![Hedge](docs/images/hedgecube.png "Hedge")

Hedge is a platform agnostic ClojureScript framework for deploying ring compatible handlers to various environments with focus on serverless deployments.

## Repository Contents

 - [/library](/library) contains wrapping and transformation code for adapting handlers to various platforms
 - [/boot](/boot) contains Hedge specific Boot tasks for code generation
 - [/acceptance](/acceptance) contains Concordion based acceptance test suite which also works as technical documentation on how to use Hedge

## Known Limitations

 - asynchronous ring handlers not yet supported

## Supported Platforms

### [Azure Functions](https://azure.microsoft.com/en-us/services/functions/)

## License

Copyright © 2016-2017 [Siili Solutions Plc.](http://www.siili.com)

Distributed under the [Eclipse Public License 1.0.](https://www.eclipse.org/legal/epl-v10.html)

## Table of Contents
1. Forewords
1. Preparations and Required Software
1. How Hedge Works
1. Preparing Hedge Authentication Against Azure
1. Authentication for AWS (tba)
1. Supported Handler Types
1. Basic Serverless Function Project Structure
1. Testing
1. Deploying to Azure
1. Deploying to AWS   

### Forewords

This document gets you started writing and deploying serverless functions to different environments. Hedge is under development, so things can change.

### Preparations and Required Software

* Java JDK 8 - Boot runs on JVM
* [CLJ-Boot](https://github.com/boot-clj/boot) - Required to build ClojureScript projects and use Hedge
* [Node.js](https://nodejs.org/en/download/) - Required for running unit tests
* [AZ CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest) - Can be used when creating Service Principal for Azure and managing Azure resources

### How Hedge Works

ClojureScript is compiled and optimized into JavaScript than can be run on Node.js or similair runtime environment available in serverless environments. Hedge takes care of generating code that is compatible and runnable on the deployment target. Hedge can then use provided authentication profile and deploy the compiled code to the serverless environment.

### Preparing Hedge Authentication Against Azure

Requirements: You must be subscription owner to be able to create service principals, or ask a subscription owner to generate the principal for you. This document describes how you can create service principal with AZ CLI from the command line.

If you haven't already performed log in with AZ CLI, type

    az cli

Make sure you are using correct subscription

    az account list --output table

Optionally change the subscription

    az account set --subscription "<your subscription name>"

Example: Create Service Principal with the name "MyNameServicePrincipal" 

    az ad sp create-for-rbac --name "MyNameServicePrincipal" --sdk-auth > MyNameServicePrincipal.json 

Hint: Use a meaningful name that you can identify, so you can find it later if you need to remove it. Keep the generated file in a secure place, because it contains contributor role credentials by default that are able to affect things in your whole subscription.

Configure your environment

    export AZURE_AUTH_LOCATION=/path-to-your/MyNameServicePrincipal.json

### Authentication Against AWS (tba)

to be written

### Supported Handler Types

Currently supported handler types are :
* HTTP IN - HTTP OUT Handler (HTTP in trigger) 

Other handler types are planned.

### Basic Serverless Project Structure

A basic structure can be found in one of the examples in the **(Example TBA)** folder.

* src/ - This directory contains your CLJS source files
* target/ - Optionally persisted compiled output directory
* resources/hedge.edn - Includes configuration information that maps your program function to the serverless function entry point. You can configure here if a function is protected with authentication code or available without authentication. 
* test/ - This directory contains your unit tests
* boot.properties - Boot properties
* build.boot - Your build configuration file
* package.json - Optionally, if you use external Node modules
* node_modules - Optionally, if you have installed Node modules

### Testing

Testing your function (unit testing) requires a JavaScript runtime, ie. Node.js (or PhantomJS) because ClojureScript is compiled to JavaScript and needs a platform to run on.

To run unit tests in test/ -folder

    boot test

To run unit tests when files change in your project (watch project)

    boot watch test

### Deploying To Azure

To deploy, Hedge requires that you create your Resource Group. Hedge can create a Storage Account and Function App for you, but we recommend that you create these by your self sp that you can choose where you host your serverless function code. One Function App can contain multiple serverless functions. 

When you create these by your self you can choose location of data center, storage options and service plan. 

Storage account is required to store your function code and logs that your functions create and other settings.

You can do it from the [Azure Portal](https://portal.azure.com) or use the CLI. 

Example create the Resource Group in northeurope data center:

    az group create --name NameOfResourceGroup --location northeurope

To create your storage account with cheapest storage option in north europe:

    az storage account create --name NameOfStorageAccount --location northeurope --resource-group NameOfResourceGroup --sku Standard_LRS

To create your function app with consumption plan (windows backed serverless runtime with dynamic resource scaling):

    az functionapp create --name NameOfFunctionApp --storage-account NameOfStorageAccount --resource-group NameOfResourceGroup --consumption-plan-location northeurope

To compile and deploy your function to Azure:

    boot hedge-azure -a NameOfFunctionApp -r NameOfResourceGroup

If your authentication file is correctly generated and found in the environment, your function should deploy to Azure and can be reached with HTTP. 

### Deploying To AWS

To be written
