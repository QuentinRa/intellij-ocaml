# README

* Add a cell renderer?

```java
// initTree
// myTree.setCellRenderer(new ProjectStructureElementRenderer(myContext));
```

* Show SdkEditor Tabs

```java
// final SdkPathEditor pathEditor=OrderRootTypeUIFactory.FACTORY.getByKey(type).createPathEditor(mySdk);
```

* was Removed from `initAdditionalDataConfigurable`

```
//            for (SdkEditorAdditionalOptionsProvider factory : SdkEditorAdditionalOptionsProvider.getSdkOptionsFactory(mySdk.getSdkType())) {
//                AdditionalDataConfigurable options = factory.createOptions(myProject, mySdk);
//                if (options != null) {
//                    configurables.add(options);
//                }
//            }
```