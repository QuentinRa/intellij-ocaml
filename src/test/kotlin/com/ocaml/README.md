# Testing Reference

Tests for IDE Features

|              | Status | Note                               |
|--------------|--------|------------------------------------|
| Annotator    | ✅      | Tested Variables colors            |
| Colors       | N/A    |                                    |
| Commenter    | ✅      | Tested line and block comments     |
| Files        | N/A    |                                    |
| Highlight    | ❌      |                                    |
| LineMarkers  | ❌      | Ensure elements have their markers |
| Presentation | ❌      | Check the presentation             |
| Settings     | N/A    |                                    |
| Spelling     | N/A    |                                    |
| Structure    | ❌      | Test getChildren                   |
| Template     | N/A    |                                    |
| Typing       | ❌      | Test Brace Matching                |

Tests for Parser

|      | Parser | Mixin | Stubs | Indexes |
|------|--------|-------|-------|---------|
| File | N/A    | N/A   | ❌     | ❌       |
| Let  | ❌      | ✅     | ❌     | ❌       |
| Val  | ❌      | ❌     | ❌     | ❌       |