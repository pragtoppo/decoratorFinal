# Design Pattern Visualizer

A Java-based graphical tool for creating and visualizing design patterns with code generation capabilities.

## Overview

Design Pattern Visualizer is an interactive application that allows users to:
- Create visual representations of design patterns
- Add and connect classes with various relationships
- Apply design pattern decorators
- Generate corresponding Java code
- Save and load diagrams

## Features

### Box Creation and Management
- Click to create new boxes representing classes
- Drag and drop boxes to reposition
- Right-click to rename boxes
- Delete boxes (coming soon)

### Design Pattern Decorators
Available decorators include:
- Observer
- Observable
- Singleton
- Decoration
- Decorator
- Chain Member
- Strategy
- Factory
- Product

### Connection Types
Supports various relationship types:
- Aggregation
- Composition
- Association
- Inheritance
- Realization

### Code Generation
- Automatically generates Java code based on the diagram
- Creates appropriate class structures
- Implements design patterns
- Updates in real-time

### File Operations
- Save diagrams (.dpv format)
- Load existing diagrams
- Create new diagrams
- Multiple file support

## How to Use

### Creating a New Diagram
1. Launch the application
2. Use left-click to place new boxes
3. Right-click boxes to:
   - Rename boxes
   - Add decorators
   - Connect decorators

### Adding Connections
1. Use the Box Connector menu
2. Select connection type
3. Click first box
4. Click second box to create connection

### Decorator Connections
1. Right-click on a box
2. Select "Connect Decorators"
3. Click first decorator
4. Click second decorator to create connection

### Generating Code
1. Create your diagram
2. Click Tools → Generate Code
3. View generated code in the Code tab

### Saving/Loading
1. File → Save to save current diagram
2. File → Open to load existing diagram
3. File → New to create new diagram

## Requirements
- Java Development Kit (JDK) 8 or higher
- Swing GUI library (included in JDK)

## Installation
1. Clone the repository:
```bash
git clone [repository-url]
```

2. Compile the project:
```bash
javac Main.java
```

3. Run the application:
```bash
java Main
```
