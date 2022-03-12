# README

* Add a cell renderer?

```java
// initTree
myTree.setCellRenderer(new ProjectStructureElementRenderer(myContext));
```

* Show SdkEditor Tabs

```java
final SdkPathEditor pathEditor=OrderRootTypeUIFactory.FACTORY.getByKey(type).createPathEditor(mySdk);
```