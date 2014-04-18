function varargout = demonstration_gui(varargin)
% DEMONSTRATION_GUI MATLAB code for demonstration_gui.fig
%      DEMONSTRATION_GUI, by itself, creates a new DEMONSTRATION_GUI or raises the existing
%      singleton*.
%
%      H = DEMONSTRATION_GUI returns the handle to a new DEMONSTRATION_GUI or the handle to
%      the existing singleton*.
%
%      DEMONSTRATION_GUI('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in DEMONSTRATION_GUI.M with the given input arguments.
%
%      DEMONSTRATION_GUI('Property','Value',...) creates a new DEMONSTRATION_GUI or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before demonstration_gui_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to demonstration_gui_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help demonstration_gui

% Last Modified by GUIDE v2.5 18-Apr-2014 15:11:23

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @demonstration_gui_OpeningFcn, ...
                   'gui_OutputFcn',  @demonstration_gui_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before demonstration_gui is made visible.
function demonstration_gui_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to demonstration_gui (see VARARGIN)

% Choose default command line output for demonstration_gui
handles.output = hObject;

global MAX_VELOCITY;
global MIN_VELOCITY;
global comPort;
global selectedRobots;
global rightDirection;
global leftDirection;
global rightVelocity;
global leftVelocity;
global NUM_ROBOTS;
global syncState;
global rightSlider;
global leftSlider;

MAX_VELOCITY = 100;
MIN_VELOCITY = 1;
NUM_ROBOTS = 6;

comPort = serial('COM7');
fopen(comPort);
selectedRobots = [0 0 0 0 0 0];
rightDirection = 0;
leftDirection = 0;
rightVelocity = MIN_VELOCITY;
leftVelocity = MIN_VELOCITY;
syncState = 0;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes demonstration_gui wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = demonstration_gui_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on slider movement.
function rightSlider_Callback(hObject, eventdata, handles)
% hObject    handle to rightSlider (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider

global MAX_VELOCITY;
global MIN_VELOCITY;
global rightVelocity;
global leftVelocity;
global leftSlider;
global rightSlider;
global syncState;

rightVelocity = get(hObject, 'Value') * (get(hObject, 'Max') - get(hObject, 'Min')) * (MAX_VELOCITY - MIN_VELOCITY) + MIN_VELOCITY;
disp(rightVelocity);

if (syncState == 1)
    leftVelocity = rightVelocity;
    
    set(leftSlider, 'Value', get(rightSlider, 'Value'));
end


% --- Executes during object creation, after setting all properties.
function rightSlider_CreateFcn(hObject, eventdata, handles)
% hObject    handle to rightSlider (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end

global rightSlider;

rightSlider = hObject;

% --- Executes on slider movement.
function leftSlider_Callback(hObject, eventdata, handles)
% hObject    handle to leftSlider (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider

global MAX_VELOCITY;
global MIN_VELOCITY;
global leftVelocity;
global rightVelocity;
global leftSlider;
global rightSlider;
global syncState;

leftVelocity = get(hObject, 'Value') * (get(hObject, 'Max') - get(hObject, 'Min')) * (MAX_VELOCITY - MIN_VELOCITY) + MIN_VELOCITY;

if (syncState == 1)
    rightVelocity = leftVelocity;
    
    set(rightSlider, 'Value', get(leftSlider, 'Value'));
end

disp(leftVelocity);

% --- Executes during object creation, after setting all properties.
function leftSlider_CreateFcn(hObject, eventdata, handles)
% hObject    handle to leftSlider (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end

global leftSlider;

leftSlider = hObject;


% --- Executes on button press in goButton.
function goButton_Callback(hObject, eventdata, handles)
% hObject    handle to goButton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

global NUM_ROBOTS;
global selectedRobots;
global leftDirection;
global rightDirection;
global leftVelocity;
global rightVelocity;
global comPort;

for i=1:NUM_ROBOTS
    disp(selectedRobots(i));
    if (selectedRobots(i) == 1)
        if leftDirection == 0
            secondByte = leftVelocity;
            thirdByte = 1;
        else
            thirdByte = leftVelocity;
            secondByte = 1;
        end
        
        if rightDirection == 0
            fourthByte = rightVelocity;
            fifthByte = 1;
        else
            fifthByte = rightVelocity;
            fourthByte = 1;
        end

        disp(strcat(num2str(i), num2str(secondByte), num2str(thirdByte), num2str(fourthByte), num2str(fifthByte), '255'));
        fwrite(comPort, [uint8(i), uint8(secondByte), uint8(thirdByte), uint8(fourthByte), uint8(fifthByte), 255]);
    end
end



% --- Executes on button press in stopButton.
function stopButton_Callback(hObject, eventdata, handles)
% hObject    handle to stopButton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

global NUM_ROBOTS;
global comPort;

for i=1:NUM_ROBOTS
    disp('Stop');
    fwrite(comPort, [i, 1, 1, 1, 1, 255]);
end

% --- Executes on button press in robot1Check.
function robot1Check_Callback(hObject, eventdata, handles)
% hObject    handle to robot1Check (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of robot1Check

global selectedRobots;

if (get(hObject, 'Value') == get(hObject, 'Min'))
    selectedRobots(1) = 0;
else
    selectedRobots(1) = 1;
end

for i=1:6
    disp(selectedRobots(i));
end

% --- Executes on button press in robot2Check.
function robot2Check_Callback(hObject, eventdata, handles)
% hObject    handle to robot2Check (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of robot2Check

global selectedRobots;

if (get(hObject, 'Value') == get(hObject, 'Min'))
    selectedRobots(2) = 0;
else
    selectedRobots(2) = 1;
end

for i=1:6
    disp(selectedRobots(i));
end

% --- Executes on button press in robot3Check.
function robot3Check_Callback(hObject, eventdata, handles)
% hObject    handle to robot3Check (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of robot3Check

global selectedRobots;

if (get(hObject, 'Value') == get(hObject, 'Min'))
    selectedRobots(3) = 0;
else
    selectedRobots(3) = 1;
end

for i=1:6
    disp(selectedRobots(i));
end

% --- Executes on button press in robot4Check.
function robot4Check_Callback(hObject, eventdata, handles)
% hObject    handle to robot4Check (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of robot4Check

global selectedRobots;

if (get(hObject, 'Value') == get(hObject, 'Min'))
    selectedRobots(4) = 0;
else
    selectedRobots(4) = 1;
end

for i=1:6
    disp(selectedRobots(i));
end


% --- Executes on button press in robot5Check.
function robot5Check_Callback(hObject, eventdata, handles)
% hObject    handle to robot5Check (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of robot5Check

global selectedRobots;

if (get(hObject, 'Value') == get(hObject, 'Min'))
    selectedRobots(5) = 0;
else
    selectedRobots(5) = 1;
end

for i=1:6
    disp(selectedRobots(i));
end

% --- Executes on button press in robot6Check.
function robot6Check_Callback(hObject, eventdata, handles)
% hObject    handle to robot6Check (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of robot6Check

global selectedRobots;

if (get(hObject, 'Value') == get(hObject, 'Min'))
    selectedRobots(6) = 0;
else
    selectedRobots(6) = 1;
end

for i=1:6
    disp(selectedRobots(i));
end


% --- Executes when selected object is changed in RightDirection.
function RightDirection_SelectionChangeFcn(hObject, eventdata, handles)
% hObject    handle to the selected object in RightDirection 
% eventdata  structure with the following fields (see UIBUTTONGROUP)
%	EventName: string 'SelectionChanged' (read only)
%	OldValue: handle of the previously selected object or empty if none was selected
%	NewValue: handle of the currently selected object
% handles    structure with handles and user data (see GUIDATA)

global rightDirection;

rightDirection = mod(uint8(rightDirection) + 1, 2);

disp(rightDirection);


% --- Executes when selected object is changed in LeftDirection.
function LeftDirection_SelectionChangeFcn(hObject, eventdata, handles)
% hObject    handle to the selected object in LeftDirection 
% eventdata  structure with the following fields (see UIBUTTONGROUP)
%	EventName: string 'SelectionChanged' (read only)
%	OldValue: handle of the previously selected object or empty if none was selected
%	NewValue: handle of the currently selected object
% handles    structure with handles and user data (see GUIDATA)

global leftDirection;

leftDirection = mod(uint8(leftDirection)+1, 2);

disp(leftDirection);


% --- Executes on button press in syncCheckbox.
function syncCheckbox_Callback(hObject, eventdata, handles)
% hObject    handle to syncCheckbox (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of syncCheckbox

global syncState;

syncState = mod((syncState + 1), 2);