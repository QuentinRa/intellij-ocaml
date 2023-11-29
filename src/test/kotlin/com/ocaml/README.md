# Testing Reference

Tests for IDE Features

|              | Status | Note                                                     |
|--------------|--------|----------------------------------------------------------|
| Annotator    | ❌      | Check that elements are annotated with the correct color |
| Colors       | N/A    |                                                          |
| Commenter    | ✅      | Test line and block comments                             |
| LineMarkers  | ❌      | Ensure elements have their markers                       |
| Presentation | ❌      | Check the presentation                                   |
| Settings     | N/A    |                                                          |
| Spelling     | N/A    |                                                          |
| Structure    | ❌      | Test getChildren                                         |
| Template     | N/A    |                                                          |
| Typing       | ❌      | Test Brace Matching                                      |

Tests for Parser

|      | Parser | Mixin | Stubs | Indexes |
|------|--------|-------|-------|---------|
| File | ❌      | ❌     | ❌     | ❌       |
| Let  | ❌      | ✅     | ❌     | ❌       |
| Val  | ❌      | ❌     | ❌     | ❌       |